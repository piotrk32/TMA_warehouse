package com.example.tma_warehouse.models.RowRequest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RowRequestInputDTO(
        @NotNull(message = "Item ID cannot be null.")
        Long itemId,

        @NotNull(message = "Quantity cannot be null.")
        @Min(value = 1, message = "Quantity must be greater than zero.")
        BigDecimal quantity,
        String comment
) {

}
