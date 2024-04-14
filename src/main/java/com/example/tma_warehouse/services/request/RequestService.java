package com.example.tma_warehouse.services.request;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.exceptions.InsufficientQuantityException;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import com.example.tma_warehouse.repositories.RequestRepository;
import com.example.tma_warehouse.security.services.FineGrainServices;
import com.example.tma_warehouse.services.employee.EmployeeService;
import com.example.tma_warehouse.services.item.ItemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ItemService itemService;
    private final EmployeeService employeeService;
    private final FineGrainServices fineGrainServices;

    @PersistenceContext
    private EntityManager entityManager;

    public Request getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "No request found with id: " + requestId));
        return request;
    }

    @Transactional
    public Request createRequest(RequestInputDTO requestInputDTO) {
        String userEmail = fineGrainServices.getUserEmail();
        Employee employee = employeeService.findEmployeeByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found for the email: " + userEmail));

        Item item = itemService.getItemById(requestInputDTO.itemId());

        BigDecimal availableQuantity = item.getQuantity();
        BigDecimal requestedQuantity = requestInputDTO.quantity();
        if (availableQuantity.compareTo(requestedQuantity) < 0) {
            throw new InsufficientQuantityException("Requested quantity for item ID " + requestInputDTO.itemId()
                    + " exceeds available stock. Available: " + availableQuantity
                    + ", Requested: " + requestedQuantity);
        }

        // Calculate priceWithoutVat as item's price multiplied by the requested quantity
        BigDecimal priceWithoutVat = item.getPriceWithoutVat().multiply(requestedQuantity);

        // Reduce the item's quantity by the requested quantity
        item.setQuantity(availableQuantity.subtract(requestedQuantity));
        itemService.saveItem(item);

        // Fetch the next value from the sequence for request_row_id
        Long requestRowId = (Long) entityManager.createNativeQuery("SELECT nextval('item_row_id_seq')")
                .getSingleResult();

        // Create and populate the Request object
        Request request = new Request(employee, item,
                item.getUnitOfMeasurement(),
                requestedQuantity,
                priceWithoutVat,
                requestInputDTO.comment(),
                RequestStatus.NEW);
        request.setRequestRowId(requestRowId);

        return requestRepository.saveAndFlush(request);
    }

    @Transactional
    public Request updateRequestById(RequestInputDTO requestInputDTO, Long requestId) {
        // Fetch the existing request
        Request request = getRequestById(requestId);

        Item item = request.getItem();

        // Calculate the difference in quantity
        BigDecimal oldQuantity = request.getQuantity();
        BigDecimal newQuantity = requestInputDTO.quantity();
        BigDecimal quantityDifference = newQuantity.subtract(oldQuantity);

        // Adjust the item's quantity
        if (quantityDifference.compareTo(BigDecimal.ZERO) != 0) {
            // Validate that the item has enough quantity available if quantity is being increased
            if (quantityDifference.compareTo(BigDecimal.ZERO) > 0 && item.getQuantity().compareTo(quantityDifference) < 0) {
                throw new InsufficientQuantityException("Not enough item stock to fulfill the request update. Available: "
                        + item.getQuantity() + ", Needed: " + quantityDifference);
            }
            item.setQuantity(item.getQuantity().subtract(quantityDifference));
            itemService.saveItem(item); // Assuming this method exists and properly updates the item
        }

        // Recalculate price with new quantity
        if (newQuantity.compareTo(oldQuantity) != 0) {
            BigDecimal newPriceWithoutVat = item.getPriceWithoutVat().multiply(newQuantity);
            request.setPriceWithoutVAT(newPriceWithoutVat);
            request.setQuantity(newQuantity);
        }

        // Update comment if provided
        if (requestInputDTO.comment() != null) {
            request.setComment(requestInputDTO.comment());
        }

        // Save the updated request
        return requestRepository.saveAndFlush(request);
    }

    public Request changeRequestStatus(Long requestId, RequestStatus newStatus) {
        // Fetch the existing request
        Request request = getRequestById(requestId);

        // Check if the current status is ARCHIVED and block status change if true
        if (request.getStatus() == RequestStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot change the status of an archived request.");
        }

        // Update the status
        request.setStatus(newStatus);

        // Save and return the updated request
        return requestRepository.saveAndFlush(request);
    }

    //MOVE TO ADMINISTRATOR
    @PreAuthorize("(#status != 'ARCHIVED') or hasAuthority('ROLE_ADMINISTRATOR')")
    public void deleteRequest(Long requestId) {
        Request request = getRequestById(requestId);
        if (request.getStatus() == RequestStatus.ARCHIVED && !fineGrainServices.hasRole("ROLE_ADMINISTRATOR")) {
            throw new IllegalStateException("Only administrators can delete archived requests.");
        }
        requestRepository.delete(request);
    }





}
