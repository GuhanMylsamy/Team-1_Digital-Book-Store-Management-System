package com.libraryManagement.project.dto.responseDTO;

import com.libraryManagement.project.entity.Book;

public class InventoryResponseDTO {
    private Long inventoryId;
    private Book book;
    private int stockQuantity;

    public InventoryResponseDTO() {}

    public InventoryResponseDTO(Long inventoryId, Book book, int stockQuantity) {
        this.inventoryId = inventoryId;
        this.book = book;
        this.stockQuantity = stockQuantity;
    }

    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}
