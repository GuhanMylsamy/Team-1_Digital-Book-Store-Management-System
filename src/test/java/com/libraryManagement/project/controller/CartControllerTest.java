package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.CartItemRequestDTO;
import com.libraryManagement.project.dto.responseDTO.CartResponseDTO;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    // Mock JwtUtil to satisfy security dependency during context loading
    @MockBean
    private JwtUtil jwtUtil;

    private CartResponseDTO cartResponseDTO;
    private final Long userId = 1L;
    private final Long bookId = 101L;

    @BeforeEach
    void setUp() {
        cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartId(1L);
        cartResponseDTO.setUserId(userId);
        cartResponseDTO.setItemCount(1);
        cartResponseDTO.setTotalAmount(99.99);
    }

    @Test
    @WithMockUser
    void testGetCart_Success() throws Exception {
        given(cartService.getCart(userId)).willReturn(cartResponseDTO);

        mockMvc.perform(get("/api/v1/cart/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.itemCount").value(1));
    }

    @Test
    @WithMockUser
    void testAddToCart_Success() throws Exception {
        CartItemRequestDTO requestDTO = new CartItemRequestDTO(bookId, 1);
        given(cartService.addToCart(anyLong(), any(CartItemRequestDTO.class))).willReturn(cartResponseDTO);

        mockMvc.perform(post("/api/v1/cart/items/user/{userId}/add", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(1L));
    }

    @Test
    @WithMockUser
    void testUpdateCartItem_Success() throws Exception {
        int quantity = 2;
        given(cartService.updateCartItem(userId, bookId, quantity)).willReturn(cartResponseDTO);

        mockMvc.perform(put("/api/v1/cart/items/user/{userId}/update/{bookId}", userId, bookId)
                        .with(csrf())
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    @WithMockUser
    void testRemoveCartItem_Success() throws Exception {
        doNothing().when(cartService).removeCartItem(userId, bookId);

        mockMvc.perform(delete("/api/v1/cart/items/user/{userId}/delete/{bookId}", userId, bookId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Item removed from cart successfully."));
    }

    @Test
    @WithMockUser
    void testClearCart_Success() throws Exception {
        doNothing().when(cartService).clearCart(userId);

        mockMvc.perform(delete("/api/v1/cart/items/user/{userId}/clear", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cart cleared successfully."));
    }
}