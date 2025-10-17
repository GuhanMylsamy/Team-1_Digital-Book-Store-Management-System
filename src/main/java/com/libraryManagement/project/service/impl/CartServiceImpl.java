package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.CartItemRequestDTO;
import com.libraryManagement.project.dto.responseDTO.CartResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Cart;
import com.libraryManagement.project.entity.CartItems;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.CartItemsRepository;
import com.libraryManagement.project.repository.CartRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemsRepository cartItemRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

//    @Override
//    public CartResponseDTO getCart(Long userId) {
//        Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found or cart is empty!"));
//        return convertToCartResponseDTO(cart);
//    }

    @Override
    public CartResponseDTO getCart(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            return convertToCartResponseDTO(cart);
        } else {
            CartResponseDTO emptyCart = new CartResponseDTO();
            emptyCart.setCartId(null);
            emptyCart.setUserId(userId);
            emptyCart.setCartItems(new ArrayList<>());
            emptyCart.setTotalAmount(0.0);
            return emptyCart;
        }
    }

    @Transactional
    @Override
    public CartResponseDTO addToCart(Long userId, CartItemRequestDTO requestDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Book book = bookRepository.findById(requestDTO.getBookId()).orElseThrow(() -> new BookNotFoundException("Book not found!"));
        if (book.getActive() != null && !book.getActive()) {
            throw new IllegalArgumentException("Book is no longer available");
        }
        if (requestDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (book.getStockQuantity() < requestDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available. Available: " + book.getStockQuantity());
        }
        Cart cart = findOrCreateCart(user);
        Optional<CartItems> existingCartItem = cartItemRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId());
        if (existingCartItem.isPresent()) {
            CartItems cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + requestDTO.getQuantity();
            if (newQuantity > book.getStockQuantity()) {
                throw new IllegalArgumentException("Not enough stock available. Available: " + book.getStockQuantity() + ", Requested: " + newQuantity);
            }
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItems cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(requestDTO.getQuantity());
            cart.addCartItem(cartItem);
        }
        cart.setUpdatedAt(LocalDateTime.now());
        Cart updatedCart = cartRepository.save(cart);
        return convertToCartResponseDTO(updatedCart);
    }

    @Transactional
    @Override
    public CartResponseDTO updateCartItem(Long userId, Long bookId, Integer quantity) {
        Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found!"));
        CartItems cartItem = cartItemRepository.findByCartCartIdAndBookId(cart.getCartId(), bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found in cart"));
        if (quantity <= 0) {
            cart.removeCartItem(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            if (quantity > book.getStockQuantity()) {
                throw new IllegalArgumentException("Not enough stock available. Available: " + book.getStockQuantity() + ", Requested: " + quantity);
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        cart.setUpdatedAt(LocalDateTime.now());
        Cart updatedCart = cartRepository.save(cart);
        return convertToCartResponseDTO(updatedCart);
    }

    @Transactional
    @Override
    public void removeCartItem(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found or cart is empty!"));
        CartItems cartItem = cartItemRepository.findByCartCartIdAndBookId(cart.getCartId(), bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found in cart"));
        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));
        List<CartItems> items = new ArrayList<>(cart.getCartItems());
        for (CartItems item : items) {
            cart.removeCartItem(item);
            cartItemRepository.delete(item);
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public Cart findOrCreateCart(User user) {
        Optional<Cart> existingCart = cartRepository.findByUserUserId(user.getUserId());
        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCreatedAt(LocalDateTime.now());
            newCart.setUpdatedAt(LocalDateTime.now());
            return cartRepository.save(newCart);
        }
    }

}