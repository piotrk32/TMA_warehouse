package com.example.tma_warehouse.models.RowRequest.dtos;

import com.example.tma_warehouse.models.RowRequest.RowRequest;

import java.time.LocalDateTime;

public class RowRequestMapper {

    public static RowRequestResponseDTO mapToRowRequestResponseDTO(RowRequest rowRequest) {

        return RowRequestResponseDTO.builder()
                .requestId(rowRequest.getRequest().getId())
                .itemId(rowRequest.getItem().getId())
                .unitOfMeasurement(rowRequest.getUnitOfMeasurement().name())
                .quantity(rowRequest.getQuantity())
                .priceWithoutVat(rowRequest.getPriceWithoutVat())
                .comment(rowRequest.getComment())
                .createdAt(LocalDateTime.parse(rowRequest.getCreatedAt().toString()))
                .modifiedAt(LocalDateTime.parse(rowRequest.getModifiedAt().toString()))
                .build();
    }
}
