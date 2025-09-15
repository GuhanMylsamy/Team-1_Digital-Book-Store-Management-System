package com.libraryManagement.project.dto.requestDTO;

import lombok.Data;

@Data
public class OrderItemRequestDTO {

    private Long itemId;
    private int quantity;
}
