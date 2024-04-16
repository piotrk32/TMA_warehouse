package com.example.tma_warehouse.services.item;

import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.dtos.ItemMapper;
import com.example.tma_warehouse.models.item.dtos.ItemRequestDTO;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.tma_warehouse.models.item.dtos.ItemMapper.mapToItemResponseDTO;


@Component
@RequiredArgsConstructor
public class ItemFacade {

    private final ItemService itemService;

    public ItemResponseDTO createItem(ItemInputDTO itemInputDTO) {
        return mapToItemResponseDTO(itemService.createItem(itemInputDTO));
    }

    public ItemResponseDTO getItemById(Long itemId) {
        Item item = itemService.getItemById(itemId);
        return mapToItemResponseDTO(item);
    }

    public void deleteItemById(Long itemId) {
        itemService.deleteItemById(itemId);
    }

    public ItemResponseDTO updateItemById(Long itemId, ItemInputDTO itemInputDTO) {
        Item updatedItem = itemService.updateItemById(itemId, itemInputDTO);
        return mapToItemResponseDTO(updatedItem);
    }

    public Page<ItemResponseDTO> getItems(ItemRequestDTO itemRequestDTO) {
        return itemService.getItems(itemRequestDTO).map(ItemMapper::mapToItemResponseDTO);
    }

}

