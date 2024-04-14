package com.example.tma_warehouse.services.request;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import com.example.tma_warehouse.repositories.RequestRepository;
import com.example.tma_warehouse.security.services.FineGrainServices;
import com.example.tma_warehouse.services.employee.EmployeeService;
import com.example.tma_warehouse.services.item.ItemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

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

        // Check if the requested quantity is available
        if (item.getQuantity().compareTo(requestInputDTO.quantity()) < 0) {
            throw new RuntimeException("Insufficient item quantity available");
        }

        // Reduce the item's quantity by the requested quantity
        item.setQuantity(item.getQuantity().subtract(requestInputDTO.quantity()));
        itemService.saveItem(item); // Now we have a saveItem method in itemService

        // Fetch the next value from the sequence for request_row_id
        Long requestRowId = (Long) entityManager.createNativeQuery("SELECT nextval('item_row_id_seq')")
                .getSingleResult();

        // Create and populate the Request object
        Request request = new Request(employee, item,
                UnitOfMeasurement.valueOf(requestInputDTO.unitOfMeasurement()),
                requestInputDTO.quantity(),
                requestInputDTO.priceWithoutVat(),
                requestInputDTO.comment(),
                RequestStatus.NEW);
        request.setRequestRowId(requestRowId);

        return requestRepository.saveAndFlush(request);
    }
    public void deleteRequestById(Long requestId) {
        Request request = getRequestById(requestId);
        requestRepository.delete(request);
    }



}
