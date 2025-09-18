package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.InventoryRequestDTO;
import com.libraryManagement.project.dto.responseDTO.InventoryResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Inventory;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.InventoryRepository;
import com.libraryManagement.project.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final BookRepository bookRepository;

    private ModelMapper modelMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, BookRepository bookRepository) {
        this.inventoryRepository = inventoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(inventory -> modelMapper.map(inventory, InventoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book inventory for requested id is not available."));
        return modelMapper.map(inventory, InventoryResponseDTO.class);
    }

    @Transactional
    @Override
    public void updateStockQuantity(Long bookId, InventoryRequestDTO requestDTO){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Inventory with requested book is not found!"));
        if (requestDTO.getStockQuantity()>=0){
            inventoryRepository.findByBook(book).ifPresent(inventory -> {
                inventory.setStockQuantity(requestDTO.getStockQuantity());
                inventoryRepository.save(inventory);
                book.setStockQuantity(requestDTO.getStockQuantity());
                bookRepository.save(book);
            });}
        else{
            throw new IndexOutOfBoundsException("Inventory stock can't be negative.");
        }
    }
    @Override
    public List<Map<String, Object>> checkLowStock() {
        return inventoryRepository.findByStockQuantityLessThan(10).stream()
                .map(inventory -> {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("inventoryId", inventory.getId());
                    alert.put("bookTitle", inventory.getBook().getTitle());
                    alert.put("currentStock", inventory.getStockQuantity());
                    return alert;
                })
                .collect(Collectors.toList());
    }

}
