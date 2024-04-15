package com.example.tma_warehouse.models.RowRequest.dtos;

import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RowRequestResponseDTO(
        Long requestId,
        Long itemId,
        String unitOfMeasurement,
        BigDecimal quantity,
        BigDecimal priceWithoutVat,
        String comment,
        String createdAt,
        String modifiedAt
) {
    @Builder
    public static RowRequestResponseDTO create(Long requestId, Long itemId, String unitOfMeasurement, BigDecimal quantity,
                                               BigDecimal priceWithoutVat,
                                            String comment, LocalDateTime createdAt,
                                            LocalDateTime modifiedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedCreatedAt = createdAt != null ? createdAt.format(formatter) : null;
        String formattedModifiedAt = modifiedAt != null ? modifiedAt.format(formatter) : null;

        return new RowRequestResponseDTO(
                requestId,
                itemId,
                unitOfMeasurement,
                quantity,
                priceWithoutVat,
                comment,
                formattedCreatedAt,
                formattedModifiedAt);
    }
}
