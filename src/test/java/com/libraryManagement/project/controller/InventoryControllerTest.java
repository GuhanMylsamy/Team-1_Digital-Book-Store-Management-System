package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.InventoryRequestDTO;
import com.libraryManagement.project.dto.responseDTO.InventoryResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = InventoryController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryServiceImpl inventoryService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private Book book;

    private InventoryResponseDTO inventoryResponseDTO;

    @BeforeEach
    void setUp() {
        when(book.getTitle()).thenReturn("The Great Gatsby");
        inventoryResponseDTO = new InventoryResponseDTO(101L, book, 15);
    }

    @Test
    @DisplayName("GET /inventory - Should Return List of All Inventories")
    void testGetAllInventory() throws Exception {
        when(inventoryService.getAllInventory()).thenReturn(List.of(inventoryResponseDTO));

        mockMvc.perform(get("/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].inventoryId").value(101L))
                .andExpect(jsonPath("$[0].book.title").value("The Great Gatsby"));
    }

    @Test
    @DisplayName("GET /inventory/get/{id} - Should Return Inventory When Found")
    void testGetInventoryById_Success() throws Exception {
        when(inventoryService.getInventoryById(101L)).thenReturn(inventoryResponseDTO);

        mockMvc.perform(get("/inventory/get/{id}", 101L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryId").value(101L))
                .andExpect(jsonPath("$.stockQuantity").value(15));
    }

    @Test
    @DisplayName("PUT /inventory/update-stock/{bookId} - Should Update and Return Success Message")
    void testUpdateStockQuantity() throws Exception {
        Long bookId = 1L;
        InventoryRequestDTO requestDTO = new InventoryRequestDTO(50, bookId);
        doNothing().when(inventoryService).updateStockQuantity(anyLong(), any(InventoryRequestDTO.class));

        mockMvc.perform(put("/inventory/update-stock/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inventory stocks updated successfully!"));

        verify(inventoryService, times(1)).updateStockQuantity(eq(bookId), any(InventoryRequestDTO.class));
    }

    @Test
    @DisplayName("GET /inventory/low-stock-alerts - Should Return Low Stock Data")
    void testGetLowStockAlerts() throws Exception {
        List<Map<String, Object>> lowStockAlerts = List.of(
                Map.of("inventoryId", 102L, "bookTitle", "1984", "currentStock", 5)
        );
        when(inventoryService.checkLowStock()).thenReturn(lowStockAlerts);

        mockMvc.perform(get("/inventory/low-stock-alerts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bookTitle").value("1984"))
                .andExpect(jsonPath("$[0].currentStock").value(5));
    }
}