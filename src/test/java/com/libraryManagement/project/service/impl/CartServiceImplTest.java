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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Book book;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);

        book = new Book();
        book.setBookId(101L);
        book.setTitle("Test Book");
        book.setPrice(50.0);
        book.setStockQuantity(10);
        book.setActive(true);

        cart = new Cart();
        cart.setCartId(1L);
        cart.setUser(user);
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setCartItems(new ArrayList<>());
    }

    // --- Tests for getCart ---
    @Test
    void testGetCart_whenCartExists_thenReturnCartDTO() {
        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        CartResponseDTO response = cartService.getCart(user.getUserId());
        assertNotNull(response);
        assertEquals(user.getUserId(), response.getUserId());
    }

    @Test
    void testGetCart_whenCartNotFound_thenThrowException() {
        given(cartRepository.findByUserUserId(anyLong())).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> cartService.getCart(user.getUserId()));
    }

    // --- Tests for addToCart ---
    @Test
    void testAddToCart_whenNewItem_thenCreateCartItem() {
        CartItemRequestDTO request = new CartItemRequestDTO(book.getBookId(), 2);
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(bookRepository.findById(book.getBookId())).willReturn(Optional.of(book));
        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        given(cartItemsRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId())).willReturn(Optional.empty());
        given(cartRepository.save(any(Cart.class))).willReturn(cart);

        CartResponseDTO response = cartService.addToCart(user.getUserId(), request);

        assertEquals(1, response.getItemCount());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddToCart_whenItemExists_thenUpdateQuantity() {
        CartItemRequestDTO request = new CartItemRequestDTO(book.getBookId(), 1);
        CartItems existingItem = new CartItems();
        existingItem.setBook(book);
        existingItem.setQuantity(2);
        cart.addCartItem(existingItem);

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(bookRepository.findById(book.getBookId())).willReturn(Optional.of(book));
        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        given(cartItemsRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId())).willReturn(Optional.of(existingItem));
        given(cartRepository.save(any(Cart.class))).willReturn(cart);

        cartService.addToCart(user.getUserId(), request);

        assertEquals(3, existingItem.getQuantity());
        verify(cartItemsRepository, times(1)).save(existingItem);
    }

    @Test
    void testAddToCart_whenNotEnoughStock_thenThrowException() {
        CartItemRequestDTO request = new CartItemRequestDTO(book.getBookId(), 11); // Stock is 10
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(bookRepository.findById(book.getBookId())).willReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(user.getUserId(), request));
    }

    // --- Tests for updateCartItem ---
    @Test
    void testUpdateCartItem_whenQuantityIsPositive_thenUpdateQuantity() {
        CartItems cartItem = new CartItems();
        cartItem.setBook(book);
        cartItem.setQuantity(1);

        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        given(bookRepository.findById(book.getBookId())).willReturn(Optional.of(book));
        given(cartItemsRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId())).willReturn(Optional.of(cartItem));
        given(cartRepository.save(any(Cart.class))).willReturn(cart);

        cartService.updateCartItem(user.getUserId(), book.getBookId(), 5);

        assertEquals(5, cartItem.getQuantity());
        verify(cartItemsRepository, times(1)).save(cartItem);
    }

    @Test
    void testUpdateCartItem_whenQuantityIsZero_thenRemoveItem() {
        CartItems cartItem = new CartItems();
        cartItem.setBook(book);
        cartItem.setQuantity(1);

        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        given(bookRepository.findById(book.getBookId())).willReturn(Optional.of(book));
        given(cartItemsRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId())).willReturn(Optional.of(cartItem));

        cartService.updateCartItem(user.getUserId(), book.getBookId(), 0);

        verify(cartItemsRepository, times(1)).delete(cartItem);
    }

    @Test
    void testUpdateCartItem_whenItemNotFoundInCart_thenThrowException() {
        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        given(bookRepository.findById(book.getBookId())).willReturn(Optional.of(book));
        given(cartItemsRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.updateCartItem(user.getUserId(), book.getBookId(), 1));
    }

    // --- Tests for removeCartItem ---
    @Test
    void testRemoveCartItem_Success() {
        CartItems cartItem = new CartItems();
        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));
        given(cartItemsRepository.findByCartCartIdAndBookId(cart.getCartId(), book.getBookId())).willReturn(Optional.of(cartItem));

        cartService.removeCartItem(user.getUserId(), book.getBookId());

        verify(cartItemsRepository, times(1)).delete(cartItem);
        verify(cartRepository, times(1)).save(cart);
    }

    // --- Tests for clearCart ---
    @Test
    void testClearCart_Success() {
        CartItems item1 = new CartItems();
        CartItems item2 = new CartItems();
        cart.addCartItem(item1);
        cart.addCartItem(item2);

        given(cartRepository.findByUserUserId(user.getUserId())).willReturn(Optional.of(cart));

        cartService.clearCart(user.getUserId());

        // Verifies that delete is called for each item in the cart
        verify(cartItemsRepository, times(2)).delete(any(CartItems.class));
        verify(cartRepository, times(1)).save(cart);
    }
}