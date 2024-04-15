package com.example.tma_warehouse.models.RowRequest.dtos;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;

public class RowRequestMapper {

    public static RowRequestResponseDTO mapToRowRequestResponseDTO(RowRequest rowRequest) {
        if (rowRequest == null) {
            return null;
        }

        return RowRequestResponseDTO.builder()
                .requestRowId(rowRequest.getId())  // The primary key of the RowRequest
                .requestId(rowRequest.getRequest().getId())  // The foreign key linking to the Request
                .itemId(rowRequest.getItem().getId())  // The foreign key linking to the Item
                .unitOfMeasurement(rowRequest.getUnitOfMeasurement().name())  // The unit of measurement as a string
                .quantity(rowRequest.getQuantity())  // The quantity from RowRequest
                .priceWithoutVat(rowRequest.getPriceWithoutVAT())  // The price without VAT
                .comment(rowRequest.getComment())  // Any comment associated with the RowRequest
                .createdAt(rowRequest.getCreatedAt().toString())  // Created at timestamp, converted to string
                .modifiedAt(rowRequest.getModifiedAt().toString())  // Modified at timestamp, converted to string
                .build();
    }
}
