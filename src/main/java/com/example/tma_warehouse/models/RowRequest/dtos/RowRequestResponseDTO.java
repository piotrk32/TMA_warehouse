package com.example.tma_warehouse.models.RowRequest.dtos;

import lombok.Builder;

import java.math.BigDecimal;

public record RowRequestResponseDTO(
        Long requestId,
        String employeeName,
        Long itemId,
        String unitOfMeasurement,
        BigDecimal quantity,
        BigDecimal priceWithoutVat,
        String comment,
        String status,
        Long requestRowId,
        String createdAt,
        String modifiedAt
) {
    @Builder
    public RowRequestResponseDTO {}
}
