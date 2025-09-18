package com.libraryManagement.project.controller;

import com.libraryManagement.project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {


    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getCart(@PathVariable Long userId) {
        Object cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/items/add/{bookId}")
    public ResponseEntity<Object> addToCart(@PathVariable Long userId, @PathVariable Long bookId, @RequestParam(defaultValue = "1") int quantity) {
        Object updatedCart = cartService.addToCart(userId, bookId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PutMapping("/{userId}/items/update/{bookId}")
    public ResponseEntity<Object> updateCartItem(@PathVariable Long userId, @PathVariable Long bookId, @RequestParam Integer quantity) {
        Object updatedCart = cartService.updateCartItem(userId, bookId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/items/delete/{bookId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long userId, @PathVariable Long bookId) {
        cartService.removeCartItem(userId, bookId);
        return ResponseEntity.ok("Item removed from cart successfully.");
    }

    @DeleteMapping("/{userId}/items/clear")
    public ResponseEntity<Map<String, String>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully."));
    }
}