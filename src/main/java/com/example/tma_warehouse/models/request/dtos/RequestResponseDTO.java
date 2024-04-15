package com.example.tma_warehouse.models.request.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RequestResponseDTO(
        Long requestId,
        String employeeName,
        BigDecimal priceWithoutVat,
        String comment,
        String status,
        //consider later
        String createdAt,
        String modifiedAt
) {
    @Builder
    public static RequestResponseDTO create(Long requestId, String employeeName, BigDecimal priceWithoutVat,
                                            String comment, String status, LocalDateTime createdAt,
                                            LocalDateTime modifiedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedCreatedAt = createdAt != null ? createdAt.format(formatter) : null;
        String formattedModifiedAt = modifiedAt != null ? modifiedAt.format(formatter) : null;

        return new RequestResponseDTO(
                requestId,
                employeeName,
                priceWithoutVat,
                comment,
                status,
                formattedCreatedAt,
                formattedModifiedAt);
    }






}
