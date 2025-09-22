package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.CartItemRequestDTO;
import com.libraryManagement.project.dto.responseDTO.CartResponseDTO;
import com.libraryManagement.project.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable("userId") Long userId) {
        CartResponseDTO cart = cartService.getCart(userId);
        return  ResponseEntity.ok(cart);
    }
    @PostMapping("/items/user/{userId}/add")
    public ResponseEntity<CartResponseDTO> addToCart(@PathVariable("userId") Long userId, @RequestBody CartItemRequestDTO requestDTO) {
        CartResponseDTO updatedCart = cartService.addToCart(userId, requestDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }
    @PutMapping("/items/user/{userId}/update/{bookId}")
    public ResponseEntity<CartResponseDTO> updateCartItem( @PathVariable("bookId") Long bookId, @PathVariable("userId") Long userId, @RequestParam Integer quantity) {
        CartResponseDTO updatedCart = cartService.updateCartItem(userId, bookId, quantity);
        return  ResponseEntity.ok(updatedCart);
    }
    @DeleteMapping("/items/user/{userId}/delete/{bookId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long bookId, @PathVariable("userId") Long userId) {
        cartService.removeCartItem(userId, bookId);
        return ResponseEntity.ok("Item removed from cart successfully.");
    }
    @DeleteMapping("/items/user/{userId}/clear")
    public ResponseEntity<Map<String,String>> clearCart(@PathVariable("userId") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(Map.of("message","Cart cleared successfully."));
    }
}