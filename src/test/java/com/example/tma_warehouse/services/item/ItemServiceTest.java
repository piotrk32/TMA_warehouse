package com.example.tma_warehouse.services.item;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.enums.ItemGroup;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setItemName("Test Item");
    }

    @Test
    void saveItem_returnsSavedItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item savedItem = itemService.saveItem(item);

        assertEquals("Test Item", savedItem.getItemName());
        verify(itemRepository).save(any(Item.class));
    }
    @Test
    void getItemById_returnsItem() {

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item foundItem = itemService.getItemById(1L);

        assertEquals("Test Item", foundItem.getItemName());
        verify(itemRepository).findById(1L);
    }
    @Test
    void getItemById_throwsExceptionWhenNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L));
        verify(itemRepository).findById(1L);
    }

    @Test
    void createItem_returnsNewItem() {

        ItemInputDTO itemInputDTO = new ItemInputDTO(
                "New Item",
                "GROUP_A",
                "t",
                new BigDecimal("10"),
                new BigDecimal("99.99"),
                "AVAILABLE",
                "A1",
                "John Doe",
                "/images/item.png");

        Item newItem = new Item(
                "New Item",
                ItemGroup.GROUP_A,
                UnitOfMeasurement.t,
                new BigDecimal("10"),
                new BigDecimal("99.99"),
                "AVAILABLE",
                "A1",
                "John Doe",
                "/images/item.png");

        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(newItem);

        Item createdItem = itemService.createItem(itemInputDTO);

        assertEquals("New Item", createdItem.getItemName());
        verify(itemRepository).saveAndFlush(any(Item.class));
    }

    @Test
    void deleteItemById_deletesItem() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.deleteItemById(1L);

        verify(itemRepository).delete(item);
        verify(itemRepository).findById(1L);
    }
}