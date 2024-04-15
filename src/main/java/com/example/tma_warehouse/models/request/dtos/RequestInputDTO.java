package com.example.tma_warehouse.models.request.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RequestInputDTO(

        String comment

) {
}
