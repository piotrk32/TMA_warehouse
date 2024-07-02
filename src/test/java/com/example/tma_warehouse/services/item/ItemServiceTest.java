package com.example.tma_warehouse.services.item;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.dtos.ItemRequestDTO;
import com.example.tma_warehouse.models.item.enums.ItemGroup;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Test
    void updateItemById_updatesAndReturnsItem() {

        ItemInputDTO itemInputDTO = new ItemInputDTO(
                "Updated Item", "GROUP_B", "kg", new BigDecimal(15), new BigDecimal("149.99"), "AVAILABLE", "A1", "Jane Doe", "/images/updated_item.png");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item);

        Item updatedItem = itemService.updateItemById(1L, itemInputDTO);

        assertEquals("Updated Item", updatedItem.getItemName());
        assertEquals(new BigDecimal(15), updatedItem.getQuantity());
        verify(itemRepository).findById(1L);
        verify(itemRepository).saveAndFlush(any(Item.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getItems_returnsPageOfItems() {
        // Arrange
        ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
        itemRequestDTO.setPage("0");
        itemRequestDTO.setSize("10");
        itemRequestDTO.setDirection("ASC");
        itemRequestDTO.setSortParam("itemName");

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "itemName");
        Item item = new Item(
                "ItemName",
                ItemGroup.GROUP_A,
                UnitOfMeasurement.t,
                new BigDecimal("10"),
                new BigDecimal("99.99"),
                "AVAILABLE",
                "A1",
                "John Doe",
                "/images/item.png");

        Page<Item> page = new PageImpl<>(List.of(item), pageRequest, 1);

        when(itemRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(page);

        Page<Item> itemsPage = itemService.getItems(itemRequestDTO);

        System.out.println("Total Elements: " + itemsPage.getTotalElements());
        System.out.println("Items: " + itemsPage.getContent());

        assertEquals(1, itemsPage.getTotalElements());
        verify(itemRepository).findAll(any(Specification.class), eq(pageRequest));
    }
}