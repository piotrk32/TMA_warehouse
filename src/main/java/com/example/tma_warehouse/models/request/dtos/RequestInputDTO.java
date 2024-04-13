package com.example.tma_warehouse.models.request.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RequestInputDTO(
        @NotBlank(message = "Employee name must be filled in.")
        String employeeName,

        @NotNull(message = "Item ID cannot be null.")
        Long itemId,

        @NotBlank(message = "Unit of measurement must be filled in.")
        String unitOfMeasurement,

        @NotNull(message = "Quantity cannot be empty.")
        @Min(value = 0, message = "Quantity must not be less than zero.")
        BigDecimal quantity,

        @NotNull(message = "Price without VAT cannot be null.")
        @Min(value = 0, message = "Price must not be less than zero.")
        BigDecimal priceWithoutVat,

        String comment
        // Status and Request Row ID are likely to be managed by the backend logic and not provided by the user.
) {
