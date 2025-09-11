package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.service.CartService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService {

    // In-memory storage for carts: Map<UserId, Map<BookId, Quantity>>
    private static final Map<Long, Map<Long, Integer>> userCarts = new ConcurrentHashMap<>();

    @Override
    public Object getCart(Long userId) {
        return userCarts.getOrDefault(userId, Map.of());
    }

    @Override
    public Object addToCart(Long userId, Long bookId, int quantity) {
        Map<Long, Integer> cart = userCarts.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        cart.merge(bookId, quantity, Integer::sum);
        return cart;
    }

    @Override
    public Object updateCartItem(Long userId, Long bookId, Integer quantity) {
        Map<Long, Integer> cart = userCarts.get(userId);
        if (cart != null && cart.containsKey(bookId)) {
            if (quantity > 0) {
                cart.put(bookId, quantity);
            } else {
                cart.remove(bookId);
            }
        }
        return cart;
    }

    @Override
    public void removeCartItem(Long userId, Long bookId) {
        Map<Long, Integer> cart = userCarts.get(userId);
        if (cart != null) {
            cart.remove(bookId);
        }
    }

    @Override
    public void clearCart(Long userId) {
        userCarts.remove(userId);
    }
}