package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.BuyNowRequestDTO;
import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.OrderItems;
import com.libraryManagement.project.service.OrderService;
import com.libraryManagement.project.service.impl.OrderServiceImpl;
import com.libraryManagement.project.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderServiceImpl orderService){
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        OrderResponseDTO orderResponseDTO = orderService.placeOrder(orderRequestDTO);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/buy-now")
    public ResponseEntity<OrderResponseDTO> buyNow(@RequestBody BuyNowRequestDTO buyNowRequestDTO){
        OrderResponseDTO orderResponseDTO = orderService.buyNow(buyNowRequestDTO);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/user")
    public ResponseEntity<List<OrderItems>> getOrdersByUser(){
        Long userId = SecurityUtil.getCurrentUserId();
        List<OrderItems> orders = orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }




}
