package com.example.tma_warehouse.services.item;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.enums.ItemGroup;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item", "No item found with id: " + itemId));
        return item;
    }

    public Item createItem(ItemInputDTO itemInputDTO) {
        ItemGroup itemGroup = ItemGroup.valueOf(itemInputDTO.itemGroup());
        UnitOfMeasurement unitOfMeasurement = UnitOfMeasurement.valueOf(itemInputDTO.unitOfMeasurement());

        Item item = new Item(
                itemGroup,
                unitOfMeasurement,
                itemInputDTO.quantity(),
                itemInputDTO.priceWithoutVat(),
                itemInputDTO.status(),
                itemInputDTO.storageLocation(),
                itemInputDTO.contactPerson(),
                itemInputDTO.photoPath()
        );
        return itemRepository.saveAndFlush(item);
    }

    public void deleteItemById(Long itemId) {
        Item item = getItemById(itemId);
        itemRepository.delete(item);
    }

    public Item updateItemById(Long itemId, ItemInputDTO itemInputDTO) {

        Item item = getItemById(itemId);

        item.setItemGroup(ItemGroup.valueOf(itemInputDTO.itemGroup()));
        item.setUnitOfMeasurement(UnitOfMeasurement.valueOf(itemInputDTO.unitOfMeasurement()));
        item.setQuantity(itemInputDTO.quantity());
        item.setPriceWithoutVat(itemInputDTO.priceWithoutVat());
        item.setStatus(itemInputDTO.status());
        item.setStorageLocation(itemInputDTO.storageLocation());
        item.setContactPerson(itemInputDTO.contactPerson());
        item.setPhotoPath(itemInputDTO.photoPath());

        return itemRepository.saveAndFlush(item);
    }





}
