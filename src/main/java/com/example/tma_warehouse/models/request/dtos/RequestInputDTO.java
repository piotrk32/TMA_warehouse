package com.example.tma_warehouse.models.request.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RequestInputDTO(

        @NotNull(message = "Item ID cannot be null.")
        Long itemId,

        @NotNull(message = "Quantity cannot be empty.")
        @Min(value = 0, message = "Quantity must not be less than zero.")
        BigDecimal quantity,
        String comment

) {
}
