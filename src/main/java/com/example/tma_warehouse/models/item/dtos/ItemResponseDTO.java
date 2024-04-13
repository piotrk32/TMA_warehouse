package com.example.tma_warehouse.models.item.dtos;

import lombok.Builder;

import java.math.BigDecimal;

public record ItemResponseDTO(String itemName,
    Long itemId,
    String itemGroup,
    String unitOfMeasurement,
    BigDecimal quantity,
    BigDecimal priceWithoutVat,
    String status,
    String storageLocation,
    String contactPerson,
    String photo){

    @Builder
    public ItemResponseDTO {}
}

