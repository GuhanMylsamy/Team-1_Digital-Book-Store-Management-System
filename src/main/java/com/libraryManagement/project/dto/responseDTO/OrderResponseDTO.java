package com.libraryManagement.project.dto.responseDTO;

import com.libraryManagement.project.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private Long addressId;
    private List<OrderItemResponseDTO> items;
    private double totalAmount;
    private LocalDateTime placedAt;
}
