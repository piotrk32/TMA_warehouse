package com.example.tma_warehouse.models.item.dtos;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemInputDTO(

        @NotBlank(message = "Item name field must be filled in.")
        String itemGroup,
        @NotBlank(message = "Unit of measurement  field must be filled in.")
        String unitOfMeasurement,
        @NotNull(message = "Quantity cannot be empty.")
        @Min(value = 0, message = "Quantity must not be less than or equal to zero.")
        BigDecimal quantity,
        BigDecimal priceWithoutVat,
        String status,
        @NotBlank(message = "Storage location field must be filled in.")
        String storageLocation,
        @NotBlank(message = "Contact person field must be filled in.")
        String contactPerson,
        String photo){

}


