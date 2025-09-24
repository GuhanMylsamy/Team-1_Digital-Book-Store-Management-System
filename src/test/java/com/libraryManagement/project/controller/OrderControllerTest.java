package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.BuyNowRequestDTO;
import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.OrderItems;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
// 👇 1. Add these static imports
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private JwtUtil jwtUtil;

    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setUp() {
        orderResponseDTO = new OrderResponseDTO(
                1L, 1L, 1L, Collections.emptyList(), 100.0, LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser
    void testPlaceOrder_Success() throws Exception {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(1L, 1L, 1L, Collections.emptyList());
        given(orderService.placeOrder(any(OrderRequestDTO.class))).willReturn(orderResponseDTO);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO))
                        .with(csrf())) // 👈 2. Add CSRF token
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1L));
    }

    @Test
    @WithMockUser
    void testBuyNow_Success() throws Exception {
        BuyNowRequestDTO buyNowRequestDTO = new BuyNowRequestDTO(1L, 1L, 1L, 2);
        given(orderService.buyNow(any(BuyNowRequestDTO.class))).willReturn(orderResponseDTO);

        mockMvc.perform(post("/api/v1/orders/buy-now")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyNowRequestDTO))
                        .with(csrf())) // 👈 2. Add CSRF token
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1L));
    }

    // GET requests do not need CSRF protection, so they remain unchanged.
    @Test
    @WithMockUser
    void testGetAllOrders_Success() throws Exception {
        Order order = new Order();
        order.setOrderId(1L);
        List<Order> orders = Collections.singletonList(order);
        given(orderService.getAllOrders()).willReturn(orders);

        mockMvc.perform(get("/api/v1/orders/getAllOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1L));
    }

    @Test
    @WithMockUser
    void testGetOrdersByUserId_Success() throws Exception {
        Long userId = 1L;
        OrderItems orderItem = new OrderItems();
        orderItem.setItemId(1L);
        List<OrderItems> orderItems = Collections.singletonList(orderItem);
        given(orderService.getOrdersByUserId(userId)).willReturn(orderItems);

        mockMvc.perform(get("/api/v1/orders/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemId").value(1L));
    }
}