package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    @Transactional
    OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO);
}
