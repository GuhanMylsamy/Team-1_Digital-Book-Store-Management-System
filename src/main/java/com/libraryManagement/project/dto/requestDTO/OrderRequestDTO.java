package com.libraryManagement.project.dto.requestDTO;

import com.libraryManagement.project.entity.ShippingAddress;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private Long cartId;
    private Long addressId;
    private List<OrderItemRequestDTO> items;
}
