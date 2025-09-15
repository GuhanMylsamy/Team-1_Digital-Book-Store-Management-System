package com.libraryManagement.project.dto.responseDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private Long addressId;
    private List<OrderItemResponseDTO> items;
    private double totalAmount;
    private LocalDateTime placedAt;
}
