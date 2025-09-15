package com.libraryManagement.project.dto.requestDTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private Long addressId;
    private List<OrderItemRequestDTO> items;
}
