package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.InventoryRequestDTO;
import com.libraryManagement.project.dto.responseDTO.InventoryResponseDTO;
import com.libraryManagement.project.entity.Inventory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    List<InventoryResponseDTO> getAllInventory();

    InventoryResponseDTO getInventoryById(Long id);

    @Transactional
    void updateStockQuantity(Long bookId, InventoryRequestDTO requestDTO);

    List<Map<String, Object>> checkLowStock();

    default InventoryResponseDTO convertToResponseDTO(Inventory inventory) {
        return new InventoryResponseDTO(
                inventory.getId(),
                inventory.getBook(),
                inventory.getStockQuantity()
        );
    }
}
