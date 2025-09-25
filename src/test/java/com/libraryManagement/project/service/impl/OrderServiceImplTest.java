package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import com.libraryManagement.project.entity.*;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private ShippingAddressRepository shippingAddressRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Cart cart;
    private Book book;
    private ShippingAddress address;
    private CartItems cartItem;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);

        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");
        book.setPrice(50.0);

        cart = new Cart();
        cart.setCartId(1L);

        address = new ShippingAddress();
        address.setAddressId(1L);

        cartItem = new CartItems();
        cartItem.setBook(book);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);

        inventory = new Inventory();
        inventory.setBook(book);
        inventory.setStockQuantity(10);
    }

    @Test
    void testPlaceOrder_Success() {
        // Arrange
        // Updated to match the AllArgsConstructor of OrderRequestDTO.
        // The 'items' list is passed as null since the current service logic uses cartId.
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L, 1L, null);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(cartRepository.findById(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findById(1L)).willReturn(Optional.of(address));
        given(cartItemsRepository.findCartItemsByCartId(1L)).willReturn(Collections.singletonList(cartItem));
        given(inventoryRepository.findByBook(book)).willReturn(Optional.of(inventory));

        // Act
        OrderResponseDTO responseDTO = orderService.placeOrder(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getUserId());
        assertEquals(100.0, responseDTO.getTotalAmount()); // 50.0 * 2
        assertEquals(1, responseDTO.getItems().size());
        assertEquals("Test Book", responseDTO.getItems().get(0).getItemName());

        // Verify interactions
        verify(inventoryRepository).save(inventory);
        verify(ordersRepository).save(any(Order.class));
        verify(cartRepository).delete(cart);
    }

    @Test
    void testPlaceOrder_UserNotFound_ThrowsException() {
        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L, 1L, null);
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.placeOrder(requestDTO);
        });

        assertEquals("User does not exist", exception.getMessage());
        verify(ordersRepository, never()).save(any());
    }
}