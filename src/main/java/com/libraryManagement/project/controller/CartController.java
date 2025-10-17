package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.CartItemRequestDTO;
import com.libraryManagement.project.dto.responseDTO.CartResponseDTO;
import com.libraryManagement.project.service.CartService;
import com.libraryManagement.project.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public ResponseEntity<CartResponseDTO> getCart() {
        Long userId = SecurityUtil.getCurrentUserId();
        CartResponseDTO cart = cartService.getCart(userId);
        return  ResponseEntity.ok(cart);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/items/user/add")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartItemRequestDTO requestDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        CartResponseDTO updatedCart = cartService.addToCart(userId, requestDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/user/update/{bookId}")
    public ResponseEntity<CartResponseDTO> updateCartItem( @PathVariable("bookId") Long bookId,@RequestParam Integer quantity) {
        Long userId = SecurityUtil.getCurrentUserId();
        CartResponseDTO updatedCart = cartService.updateCartItem(userId, bookId, quantity);
        return  ResponseEntity.ok(updatedCart);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/user/delete/{bookId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long bookId) {
        Long userId = SecurityUtil.getCurrentUserId();
        cartService.removeCartItem(userId, bookId);
        return ResponseEntity.ok("Item removed from cart successfully.");
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/user/clear")
    public ResponseEntity<Map<String,String>> clearCart() {
        Long userId = SecurityUtil.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.ok(Map.of("message","Cart cleared successfully."));
    }
}