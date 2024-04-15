package com.example.tma_warehouse.models.RowRequest.dtos;

import com.example.tma_warehouse.models.RowRequest.RowRequest;

import java.time.LocalDateTime;

public class RowRequestMapper {

    public static RowRequestResponseDTO mapToRowRequestResponseDTO(RowRequest rowRequest) {

        return RowRequestResponseDTO.builder()
                .requestId(rowRequest.getRequest().getId())  // The foreign key linking to the Request
                .itemId(rowRequest.getItem().getId())  // The foreign key linking to the Item
                .unitOfMeasurement(rowRequest.getUnitOfMeasurement().name())  // The unit of measurement as a string
                .quantity(rowRequest.getQuantity())  // The quantity from RowRequest
                .priceWithoutVat(rowRequest.getPriceWithoutVat())  // The price without VAT
                .comment(rowRequest.getComment())  // Any comment associated with the RowRequest
                .createdAt(LocalDateTime.parse(rowRequest.getCreatedAt().toString()))
                .modifiedAt(LocalDateTime.parse(rowRequest.getModifiedAt().toString())) // Modified at timestamp, converted to string
                .build();
    }
}
