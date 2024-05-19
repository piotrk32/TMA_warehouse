package com.example.tma_warehouse.services.request;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.exceptions.InsufficientQuantityException;
import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.dtos.RequestRequestDTO;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import com.example.tma_warehouse.repositories.RequestRepository;
import com.example.tma_warehouse.security.services.FineGrainServices;
import com.example.tma_warehouse.services.employee.EmployeeService;
import com.example.tma_warehouse.services.item.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    @Transactional
    public Request createRequest(RequestInputDTO requestInputDTO) {

        String userEmail = fineGrainServices.getUserEmail();
        Employee employee = employeeService.findEmployeeByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found for the email: " + userEmail));


        Request newRequest = new Request();
        newRequest.setEmployee(employee);
        newRequest.setStatus(RequestStatus.NEW);


        return requestRepository.saveAndFlush(newRequest);
    }




    @Transactional
    public Request updateNewRequest(Long requestId, RequestInputDTO requestInputDTO, Long employeeId) {
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "Request not found with id: " + requestId));


        if (existingRequest.getStatus() != RequestStatus.NEW) {
            throw new IllegalStateException("Only requests with status 'NEW' can be updated.");
        }


        if (!existingRequest.getEmployee().getId().equals(employeeId)) {
            throw new SecurityException("You can only update requests that you have created.");
        }

        if (requestInputDTO.comment() != null) {
            existingRequest.setComment(requestInputDTO.comment());
        }

        return requestRepository.save(existingRequest);
    }

    @Transactional
    public Request changeRequestStatus(Long requestId, RequestStatus newStatus, String comment) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "Request not found with id: " + requestId));

        if (request.getStatus() == RequestStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot change the status of an archived request.");
        }

        if (newStatus == RequestStatus.REJECTED && (comment == null || comment.trim().isEmpty())) {
            throw new IllegalArgumentException("A comment is required when rejecting a request.");
        }

        if (request.getStatus() == RequestStatus.APPROVED && newStatus == RequestStatus.REJECTED) {
            for (RowRequest row : request.getRowRequests()) {
                Item item = row.getItem();
                item.setQuantity(item.getQuantity().add(row.getQuantity()));
                itemService.saveItem(item);
            }
        }

        if (newStatus == RequestStatus.APPROVED) {
            BigDecimal totalCost = BigDecimal.ZERO;
            for (RowRequest row : request.getRowRequests()) {
                Item item = row.getItem();
                BigDecimal newQuantity = item.getQuantity().subtract(row.getQuantity());
                if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InsufficientQuantityException("Insufficient stock for item ID " + item.getId());
                }
                BigDecimal rowCost = row.getPriceWithoutVat();
                totalCost = totalCost.add(rowCost);


                item.setQuantity(newQuantity);
                itemService.saveItem(item);
            }

            request.setPriceWithoutVAT(totalCost);
        }

        if (comment != null && !comment.trim().isEmpty()) {
            request.setComment(comment);
        }

        request.setStatus(newStatus);

        return requestRepository.save(request);
    }


    public Page<Request> getRequests(RequestRequestDTO requestRequestDTO) {

        PageRequest pageRequest = PageRequest.of(
                Integer.parseInt(requestRequestDTO.getPage()),
                Integer.parseInt(requestRequestDTO.getSize()),
                Sort.Direction.fromString(requestRequestDTO.getDirection()),
                requestRequestDTO.getSortParam());
        Specification<Request> spec = Specification.where(null);

        if (requestRequestDTO.getEmployeeNameSearch() != null) {
            spec = spec.and(RequestSpecification.hasEmployeeName(requestRequestDTO.getEmployeeNameSearch()));
        }
        if (requestRequestDTO.getStatus() != null) {
            spec = spec.and(RequestSpecification.hasStatus(requestRequestDTO.getStatus()));
        }

        if (requestRequestDTO.getPriceWithoutVatFrom() != null && requestRequestDTO.getPriceWithoutVatTo() != null) {
            spec = spec.and(RequestSpecification.priceWithoutVatBetween(
                    new BigDecimal(String.valueOf(requestRequestDTO.getPriceWithoutVatFrom())),
                    new BigDecimal(String.valueOf(requestRequestDTO.getPriceWithoutVatTo()))
            ));
        }
        if (requestRequestDTO.getComment() != null) {
            spec = spec.and(RequestSpecification.hasCommentLike(requestRequestDTO.getComment()));
        }

        Pageable pageable = PageRequest.of(
                Integer.parseInt(requestRequestDTO.getPage()),
                Integer.parseInt(requestRequestDTO.getSize()),
                Sort.by(Sort.Direction.fromString(requestRequestDTO.getDirection()), requestRequestDTO.getSortParam())
        );

        return requestRepository.findAll(spec, pageRequest);
    }



    @PreAuthorize("(#status != 'ARCHIVED') or hasAuthority('ROLE_ADMINISTRATOR')")
    public void deleteRequest(Long requestId) {
        Request request = getRequestById(requestId);
        if (request.getStatus() == RequestStatus.ARCHIVED && !fineGrainServices.hasRole("ROLE_ADMINISTRATOR")) {
            throw new IllegalStateException("Only administrators can delete archived requests.");
        }
        requestRepository.delete(request);
    }
}
