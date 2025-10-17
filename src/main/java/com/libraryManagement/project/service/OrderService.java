package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.BuyNowRequestDTO;
import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderItemResponseDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.OrderItems;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    @Transactional
    OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO);

    @Transactional
    OrderResponseDTO buyNow(BuyNowRequestDTO buyNowRequestDTO);

    List<Order> getAllOrders();

    List<OrderItemResponseDTO> getOrdersByUserId(Long userId);
}
