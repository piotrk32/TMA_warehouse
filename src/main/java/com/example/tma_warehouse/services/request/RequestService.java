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

    public Request getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "No request found with id: " + requestId));
        return request;
    }

    public Request createRequest(RequestInputDTO requestInputDTO) {
        String userEmail = fineGrainServices.getUserEmail();
        Optional<Employee> employeeOptional = employeeService.findEmployeeByEmail(userEmail);

        if (employeeOptional.isEmpty()) {
            throw new RuntimeException("Employee not found for the email: " + userEmail);
        }

        Employee employee = employeeOptional.get();

        Item item = itemService.getItemById(requestInputDTO.itemId());

        Request request = new Request();
        request.setEmployee(employee);  // Assuming Request has a setEmployee method and Employee class contains name fields
        request.setItem(item);
        request.setUnitOfMeasurement(UnitOfMeasurement.valueOf(requestInputDTO.unitOfMeasurement()));
        request.setQuantity(requestInputDTO.quantity());
        request.setPriceWithoutVAT(requestInputDTO.priceWithoutVat());
        request.setComment(requestInputDTO.comment());
        request.setStatus(RequestStatus.NEW);

        return requestRepository.saveAndFlush(request);
    }

}
