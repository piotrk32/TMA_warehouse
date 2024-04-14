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

        // Fetch the next value from the sequence
        Long requestRowId = (Long) entityManager.createNativeQuery("SELECT nextval('item_row_id_seq')")
                .getSingleResult();

        // Create and populate the Request object
        Request request = new Request();
        request.setEmployee(employee);
        request.setItem(item);
        request.setUnitOfMeasurement(UnitOfMeasurement.valueOf(requestInputDTO.unitOfMeasurement()));
        request.setQuantity(requestInputDTO.quantity());
        request.setPriceWithoutVAT(requestInputDTO.priceWithoutVat());
        request.setComment(requestInputDTO.comment());
        request.setStatus(RequestStatus.NEW);
        request.setRequestRowId(requestRowId); // Set the fetched sequence value

        return requestRepository.saveAndFlush(request);
    }
    public void deleteRequestById(Long requestId) {
        Request request = getRequestById(requestId);
        requestRepository.delete(request);
    }

}
