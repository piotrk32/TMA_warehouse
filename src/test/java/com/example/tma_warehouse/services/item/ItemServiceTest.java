package com.example.tma_warehouse.services.item;

import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.repositories.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Act
        Item foundItem = itemService.getItemById(1L);

        // Assert
        assertEquals("Test Item", foundItem.getItemName());
        verify(itemRepository).findById(1L);
    }
    @Test
    void getItemById_throwsExceptionWhenNotFound() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L));
        verify(itemRepository).findById(1L);
    }
}