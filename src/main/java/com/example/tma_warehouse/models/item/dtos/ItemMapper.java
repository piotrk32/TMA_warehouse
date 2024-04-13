package com.example.tma_warehouse.models.item.dtos;

import com.example.tma_warehouse.models.item.Item;

public class ItemMapper {

    public static ItemResponseDTO mapToItemResponseDTO(Item item) {

        return ItemResponseDTO
                .builder()
                .itemId(item.getId())
                .itemGroup(String.valueOf(item.getItemGroup()))
                .unitOfMeasurement(String.valueOf(item.getUnitOfMeasurement()))
                .quantity(item.getQuantity())
                .priceWithoutVat(item.getPriceWithoutVat())
                .status(item.getStatus())
                .storageLocation(item.getStorageLocation())
                .contactPerson(item.getContactPerson())
                .photo(item.getPhotoPath())
                .build();
    }
}
