package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.InventoryRequestDTO;
import com.libraryManagement.project.dto.responseDTO.InventoryResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Inventory;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Book book; // The mocked Book entity

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory; // This is a REAL object, not a mock.

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setId(101L);
        inventory.setBook(book);
        inventory.setStockQuantity(15);
    }

    @Test
    @DisplayName("Test getAllInventory - Should Return List of Inventory DTOs")
    void testGetAllInventory_ShouldReturnListOfInventoryDTOs() {
        // Arrange
        when(book.getTitle()).thenReturn("The Great Gatsby");
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

        // Act
        List<InventoryResponseDTO> result = inventoryService.getAllInventory();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("The Great Gatsby", result.get(0).getBook().getTitle());
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getInventoryById - Should Return Inventory DTO When Found")
    void testGetInventoryById_ShouldReturnInventoryDTO_WhenFound() {
        // Arrange
        when(book.getTitle()).thenReturn("The Great Gatsby");
        when(inventoryRepository.findById(101L)).thenReturn(Optional.of(inventory));

        // Act
        InventoryResponseDTO result = inventoryService.getInventoryById(101L);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getInventoryId());
        assertEquals(15, result.getStockQuantity());
        assertEquals("The Great Gatsby", result.getBook().getTitle());
        verify(inventoryRepository, times(1)).findById(101L);
    }

    @Test
    @DisplayName("Test getInventoryById - Should Throw Exception When Not Found")
    void testGetInventoryById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> inventoryService.getInventoryById(999L));
        verify(inventoryRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Test updateStockQuantity - Should Update Successfully")
    void testUpdateStockQuantity_ShouldUpdateSuccessfully() {
        // Arrange
        Long bookId = 1L;
        InventoryRequestDTO requestDTO = new InventoryRequestDTO(20, bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(inventoryRepository.findByBook(book)).thenReturn(Optional.of(inventory));

        // Act
        inventoryService.updateStockQuantity(bookId, requestDTO);

        // Assert
        // THE FIX IS HERE: Use assertEquals to check the state of a real object.
        assertEquals(20, inventory.getStockQuantity());

        // Use verify to check interactions with MOCK objects.
        verify(book, times(1)).setStockQuantity(20);
        verify(inventoryRepository, times(1)).save(inventory);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Test updateStockQuantity - Should Throw Exception for Non-Existent Book")
    void testUpdateStockQuantity_ShouldThrowException_ForNonExistentBook() {
        // Arrange
        InventoryRequestDTO requestDTO = new InventoryRequestDTO(20, 999L);
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> inventoryService.updateStockQuantity(999L, requestDTO));
        verify(inventoryRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test updateStockQuantity - Should Throw Exception for Negative Stock")
    void testUpdateStockQuantity_ShouldThrowException_ForNegativeStock() {
        // Arrange
        InventoryRequestDTO requestDTO = new InventoryRequestDTO(-5, 1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> inventoryService.updateStockQuantity(1L, requestDTO));

        // Verify no save operations occurred
        verify(inventoryRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test checkLowStock - Should Return Low Stock Items")
    void testCheckLowStock_ShouldReturnLowStockItems() {
        // Arrange
        when(book.getTitle()).thenReturn("1984");
        Inventory lowStockInventory = new Inventory();
        lowStockInventory.setId(102L);
        lowStockInventory.setBook(book);
        lowStockInventory.setStockQuantity(5);

        when(inventoryRepository.findByStockQuantityLessThan(10)).thenReturn(List.of(lowStockInventory));

        // Act
        List<Map<String, Object>> result = inventoryService.checkLowStock();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(102L, result.get(0).get("inventoryId"));
        assertEquals("1984", result.get(0).get("bookTitle"));
        assertEquals(5, result.get(0).get("currentStock"));
        verify(inventoryRepository, times(1)).findByStockQuantityLessThan(10);
    }

    @Test
    @DisplayName("Test checkLowStock - Should Return Empty List When No Low Stock")
    void testCheckLowStock_ShouldReturnEmptyList_WhenNoLowStock() {
        // Arrange
        when(inventoryRepository.findByStockQuantityLessThan(10)).thenReturn(Collections.emptyList());

        // Act
        List<Map<String, Object>> result = inventoryService.checkLowStock();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(inventoryRepository, times(1)).findByStockQuantityLessThan(10);
    }
}