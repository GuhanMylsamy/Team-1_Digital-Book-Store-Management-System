package com.libraryManagement.project.dto.requestDTO;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InventoryRequestDTO {
    @Min(0)
    private int stockQuantity;

    private Long bookId;
    public InventoryRequestDTO() {}

    public InventoryRequestDTO(int stockQuantity, Long bookId) {
        this.stockQuantity = stockQuantity;
        this.bookId = bookId;
    }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
}
