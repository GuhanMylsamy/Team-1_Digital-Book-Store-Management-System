package com.libraryManagement.project.service;

public interface CartService {

    Object getCart(Long userId);

    Object addToCart(Long userId, Long bookId, int quantity);

    Object updateCartItem(Long userId, Long bookId, Integer quantity);

    void removeCartItem(Long userId, Long bookId);

    void clearCart(Long userId);
}