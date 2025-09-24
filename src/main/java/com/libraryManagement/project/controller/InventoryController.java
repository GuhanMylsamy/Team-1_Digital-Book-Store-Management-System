package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.InventoryRequestDTO;
import com.libraryManagement.project.dto.responseDTO.InventoryResponseDTO;
import com.libraryManagement.project.service.impl.InventoryServiceImpl;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@PreAuthorize("hasAuthority('ADMIN')")
public class InventoryController {
    private final InventoryServiceImpl inventoryService;

    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @PutMapping("/update-stock/{bookId}")
    public ResponseEntity<Map<String,String>> updateStockQuantity(@PathVariable Long bookId, @RequestBody InventoryRequestDTO requestDTO) {
        inventoryService.updateStockQuantity(bookId, requestDTO);
        return ResponseEntity.ok(Map.of("message", "Inventory stocks updated successfully!"));
    }
    @GetMapping("/low-stock-alerts")
    public ResponseEntity<List<Map<String, Object>>> getLowStockAlerts() {
        return ResponseEntity.ok(inventoryService.checkLowStock());
    }
}
