package com.example.tma_warehouse.services.rowrequest;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.exceptions.InsufficientQuantityException;
import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestInputDTO;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import com.example.tma_warehouse.repositories.RequestRepository;
import com.example.tma_warehouse.repositories.RowRequestRepository;
import com.example.tma_warehouse.services.item.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RowRequestService {

    private final ItemService itemService;
    private final RequestRepository requestRepository;
    private final RowRequestRepository rowRequestRepository;

    public RowRequest getRowRequestById(Long rowRequestId) {
        RowRequest rowRequest = rowRequestRepository.findById(rowRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "No request found with id: " + rowRequestId));
        return rowRequest;
    }

    @Transactional
    public RowRequest addItemToRequest(Long requestId, RowRequestInputDTO rowRequestInputDTO) {
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "Request not found with id: " + requestId));

        if (existingRequest.getStatus() == RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot add items to an approved request.");
        }

        Item item = itemService.getItemById(rowRequestInputDTO.itemId());

        if (item.getQuantity().compareTo(rowRequestInputDTO.quantity()) < 0) {
            throw new InsufficientQuantityException("Requested quantity exceeds available stock for item ID " + rowRequestInputDTO.itemId());
        }

        BigDecimal priceWithoutVAT = item.getPriceWithoutVat().multiply(rowRequestInputDTO.quantity());

        RowRequest newRowRequest = new RowRequest();
        newRowRequest.setRequest(existingRequest);
        newRowRequest.setItem(item);
        newRowRequest.setUnitOfMeasurement(item.getUnitOfMeasurement());
        newRowRequest.setQuantity(rowRequestInputDTO.quantity());
        newRowRequest.setPriceWithoutVat(priceWithoutVAT);
        newRowRequest.setComment(rowRequestInputDTO.comment());

        return rowRequestRepository.save(newRowRequest);
    }

    @Transactional
    public void removeItemFromRequest(Long requestId, Long rowRequestId) {
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "Request not found with id: " + requestId));

        if (existingRequest.getStatus() == RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot remove items from an approved request.");
        }

        RowRequest rowRequest = rowRequestRepository.findById(rowRequestId)
                .orElseThrow(() -> new EntityNotFoundException("RowRequest", "RowRequest not found with id: " + rowRequestId));

        if (!rowRequest.getRequest().getId().equals(requestId)) {
            throw new IllegalStateException("RowRequest does not belong to the given Request.");
        }

        rowRequestRepository.delete(rowRequest);
    }

    public Page<RowRequest> getRowsRequest(RowRequestDTO rowRequestDTO) {

        Specification<RowRequest> spec = Specification.where(null);

        if (rowRequestDTO.getPriceWithoutVatFrom() != null && rowRequestDTO.getPriceWithoutVatTo() != null) {
            spec = spec.and(RowRequestSpecification.priceWithoutVatBetween(
                    new BigDecimal(String.valueOf(rowRequestDTO.getPriceWithoutVatFrom())),
                    new BigDecimal(String.valueOf(rowRequestDTO.getPriceWithoutVatTo()))
            ));
        }

        Pageable pageable = PageRequest.of(
                Integer.parseInt(rowRequestDTO.getPage()),
                Integer.parseInt(rowRequestDTO.getSize()),
                Sort.by(Sort.Direction.fromString(rowRequestDTO.getDirection()), rowRequestDTO.getSortParam())
        );

        return rowRequestRepository.findAll(spec, pageable);
    }


    public Page<RowRequest> getAllRows(int page, int size, String sortBy, String sortDir, Long requestId, Long itemId) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Specification<RowRequest> spec = Specification.where(null);

        if (requestId != null) {
            spec = spec.and(RowRequestSpecification.hasRequestId(requestId));
        }
        if (itemId != null) {
            spec = spec.and(RowRequestSpecification.hasItemId(itemId));
        }

        return rowRequestRepository.findAll(spec, pageable);
    }


    public Page<RowRequest> getRowsForRequest(int page, int size, String sortBy, String sortDir, Long requestId) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Specification<RowRequest> spec = Specification.where(null);

        if (requestId != null) {
            spec = spec.and(RowRequestSpecification.hasRequestId(requestId));
        }

        return rowRequestRepository.findAll(spec, pageable);
    }

    @Transactional
    public RowRequest updateRowRequestById(Long rowRequestId, RowRequestInputDTO inputDTO, Long employeeId) {
        RowRequest existingRowRequest = rowRequestRepository.findById(rowRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "Request not found with id: " + rowRequestId));

        Request existingRequest = existingRowRequest.getRequest();

        if (!existingRequest.getEmployee().getId().equals(employeeId)) {
            throw new SecurityException("You can only update rows that you have created.");
        }

        if (existingRequest.getStatus() == RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot update items in an approved request.");
        }

        Item item = itemService.getItemById(inputDTO.itemId());

        BigDecimal adjustedQuantity = item.getQuantity().add(existingRowRequest.getQuantity()).subtract(inputDTO.quantity());
        if (adjustedQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientQuantityException("Updated quantity exceeds available stock for item ID " + inputDTO.itemId());
        }

        existingRowRequest.setItem(item);
        existingRowRequest.setQuantity(inputDTO.quantity());
        existingRowRequest.setComment(inputDTO.comment());

        item.setQuantity(adjustedQuantity);
        itemService.saveItem(item);

        return rowRequestRepository.save(existingRowRequest);
    }

}
