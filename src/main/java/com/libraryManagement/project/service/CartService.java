package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.CartItemRequestDTO;
import com.libraryManagement.project.dto.responseDTO.CartItemResponseDTO;
import com.libraryManagement.project.dto.responseDTO.CartResponseDTO;
import com.libraryManagement.project.entity.Cart;
import com.libraryManagement.project.entity.CartItems;
import com.libraryManagement.project.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface CartService {
    CartResponseDTO getCart(Long userId);

    @Transactional
    CartResponseDTO addToCart(Long userId, CartItemRequestDTO requestDTO);

    @Transactional
    CartResponseDTO updateCartItem(Long userId, Long bookId, Integer quantity);

    @Transactional
    void removeCartItem(Long userId, Long bookId);

    @Transactional
    void clearCart(Long userId);

    @Transactional
    Cart findOrCreateCart(User user);

    default CartResponseDTO convertToCartResponseDTO(Cart cart) {
        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setCartId(cart.getCartId());
        responseDTO.setUserId(cart.getUser().getUserId());
        responseDTO.setUpdatedAt(cart.getUpdatedAt());
        Double totalAmount = cart.getCartItems().stream().mapToDouble(item -> item.getBook().getPrice() * item.getQuantity()).sum();
        responseDTO.setTotalAmount(totalAmount);
        responseDTO.setItemCount(cart.getCartItems().size());
        List<CartItemResponseDTO> itemDTOs = cart.getCartItems().stream().map(this::convertToCartItemResponseDTO).collect(Collectors.toList());
        responseDTO.setCartItems(itemDTOs);
        return responseDTO;
    }

    default CartItemResponseDTO convertToCartItemResponseDTO(CartItems item) {
        CartItemResponseDTO itemDTO = new CartItemResponseDTO();
        itemDTO.setCartItemId(item.getCartItemId());
        itemDTO.setBookId(item.getBook().getBookId());
        itemDTO.setBookTitle(item.getBook().getTitle());
        itemDTO.setPrice(item.getBook().getPrice());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setSubtotal(item.getBook().getPrice() * item.getQuantity());
        return itemDTO;
    }
}
