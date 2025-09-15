package com.libraryManagement.project.dto.responseDTO;

import lombok.Data;

@Data
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long itemId;
    private String itemName;
    private int quantity;
    private double price;
}
