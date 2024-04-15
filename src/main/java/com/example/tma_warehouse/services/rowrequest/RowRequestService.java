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
        // Fetch the request to which we want to add the item
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request", "Request not found with id: " + requestId));

        // Check if the request is already APPROVED, and if so, throw an exception
        if (existingRequest.getStatus() == RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot add items to an approved request.");
        }

        // Fetch the item that we want to add to the request
        Item item = itemService.getItemById(rowRequestInputDTO.itemId());

        // Check if the available quantity of the item allows for adding to the request
        if (item.getQuantity().compareTo(rowRequestInputDTO.quantity()) < 0) {
            throw new InsufficientQuantityException("Requested quantity exceeds available stock for item ID " + rowRequestInputDTO.itemId());
        }

        // Calculate the priceWithoutVAT for the RowRequest based on the item's price and the requested quantity
        BigDecimal priceWithoutVAT = item.getPriceWithoutVat().multiply(rowRequestInputDTO.quantity());

        // Create a new RowRequest and set its properties including the unit of measurement from the item
        RowRequest newRowRequest = new RowRequest();
        newRowRequest.setRequest(existingRequest);
        newRowRequest.setItem(item);
        newRowRequest.setUnitOfMeasurement(item.getUnitOfMeasurement()); // Set from the Item directly
        newRowRequest.setQuantity(rowRequestInputDTO.quantity());
        newRowRequest.setPriceWithoutVat(priceWithoutVAT);
        newRowRequest.setComment(rowRequestInputDTO.comment());

        // Save the new RowRequest in the repository
        return rowRequestRepository.save(newRowRequest);
    }

    public Page<RowRequest> getRowsRequest(RowRequestDTO rowRequestDTO) {

//        PageRequest pageRequest = PageRequest.of(
//                Integer.parseInt(rowRequestDTO.getPage()),
//                Integer.parseInt(rowRequestDTO.getSize()),
//                Sort.Direction.fromString(rowRequestDTO.getDirection()),
//                rowRequestDTO.getSortParam());
        Specification<RowRequest> spec = Specification.where(null);

//        try {
//            if (requestRequestDTO.getFromDate() != null && requestRequestDTO.getToDate() != null) {
//                spec = spec.and(RequestSpecification.createdBetween(requestRequestDTO.getFromDate(), requestRequestDTO.getToDate()));
//            }
//        } catch (DateTimeParseException e) {
//            // Log error or handle it based on your application's requirements
//            System.err.println("Error parsing dates: " + e.getMessage());
//        }
        if (rowRequestDTO.getPriceWithoutVatFrom() != null && rowRequestDTO.getPriceWithoutVatTo() != null) {
            spec = spec.and(RowRequestSpecification.priceWithoutVatBetween(
                    new BigDecimal(String.valueOf(rowRequestDTO.getPriceWithoutVatFrom())),
                    new BigDecimal(String.valueOf(rowRequestDTO.getPriceWithoutVatTo()))
            ));
        }


        // Define sorting and pagination
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

//    public Page<RowRequest> findAllByRequestId(Long requestId, Pageable pageable) {
//        Specification<RowRequest> spec = RowRequestSpecification.hasRequestId(requestId);
//        return rowRequestRepository.findAll(spec, pageable);
//    }

    public Page<RowRequest> getRowsForRequest(int page, int size, String sortBy, String sortDir, Long requestId) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Specification<RowRequest> spec = Specification.where(null);

        if (requestId != null) {
            spec = spec.and(RowRequestSpecification.hasRequestId(requestId));
        }

        return rowRequestRepository.findAll(spec, pageable);
    }

}
