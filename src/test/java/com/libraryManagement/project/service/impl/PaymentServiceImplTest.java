package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.constants.PaymentConstants;
import com.libraryManagement.project.dto.requestDTO.PaymentRequestDTO;
import com.libraryManagement.project.dto.responseDTO.PaymentResponseDTO;
import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.Payments;
import com.libraryManagement.project.enums.PaymentStatus;
import com.libraryManagement.project.exception.MalpracticeException;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.OrdersRepository;
import com.libraryManagement.project.repository.PaymentsRepository;
import com.libraryManagement.project.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private PaymentsRepository paymentsRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private PaymentRequestDTO paymentRequestDTO;
    private final Long userId = 1L;
    private final Long orderId = 10L;
    private final double amount = 250.0;

    // To mock the static method in PaymentConstants
    private MockedStatic<PaymentConstants> mockedPaymentConstants;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(orderId);
        order.setTotalAmount(amount);

        paymentRequestDTO = new PaymentRequestDTO("CARD", "1234567890", amount, 123, orderId);

        // Start mocking static methods for PaymentConstants
        mockedPaymentConstants = Mockito.mockStatic(PaymentConstants.class);
    }

    @AfterEach
    void tearDown() {
        // Close the static mock after each test to avoid test pollution
        mockedPaymentConstants.close();
    }

    @Test
    void testValidatePayment_whenPaymentSuccessful_thenReturnSuccessResponse() {
        // Arrange
        given(userRepository.existsById(userId)).willReturn(true);
        given(ordersRepository.findById(orderId)).willReturn(Optional.of(order));
        mockedPaymentConstants.when(() -> PaymentConstants.validatePayment(any(), any(), anyInt(), anyDouble()))
                .thenReturn(true);

        // Mock the save operation to return a persisted payment object
        given(paymentsRepository.save(any(Payments.class))).willAnswer(invocation -> {
            Payments payment = invocation.getArgument(0);
            payment.setPaymentId(1L); // Simulate DB assigning an ID
            return payment;
        });

        // Act
        PaymentResponseDTO response = paymentService.validatePayment(userId, paymentRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS.toString(), response.getStatus());
        assertNotNull(response.getTransactionId());
        verify(paymentsRepository).save(any(Payments.class));
    }

    @Test
    void testValidatePayment_whenPaymentFails_thenReturnFailedResponse() {
        // Arrange
        given(userRepository.existsById(userId)).willReturn(true);
        given(ordersRepository.findById(orderId)).willReturn(Optional.of(order));
        mockedPaymentConstants.when(() -> PaymentConstants.validatePayment(any(), any(), anyInt(), anyDouble()))
                .thenReturn(false);

        given(paymentsRepository.save(any(Payments.class))).willAnswer(invocation -> {
            Payments payment = invocation.getArgument(0);
            payment.setPaymentId(1L);
            return payment;
        });

        // Act
        PaymentResponseDTO response = paymentService.validatePayment(userId, paymentRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(PaymentStatus.FAILED.toString(), response.getStatus());
        verify(paymentsRepository).save(any(Payments.class));
    }

    @Test
    void testValidatePayment_whenUserNotFound_thenThrowException() {
        // Arrange
        given(userRepository.existsById(userId)).willReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.validatePayment(userId, paymentRequestDTO);
        });
        verify(paymentsRepository, never()).save(any());
    }

    @Test
    void testValidatePayment_whenOrderNotFound_thenThrowException() {
        // Arrange
        given(userRepository.existsById(userId)).willReturn(true);
        given(ordersRepository.findById(orderId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.validatePayment(userId, paymentRequestDTO);
        });
        verify(paymentsRepository, never()).save(any());
    }

    @Test
    void testValidatePayment_whenAmountMismatches_thenThrowMalpracticeException() {
        // Arrange
        paymentRequestDTO.setAmount(amount - 50.0); // Create a mismatch
        given(userRepository.existsById(userId)).willReturn(true);
        given(ordersRepository.findById(orderId)).willReturn(Optional.of(order));

        // Act & Assert
        MalpracticeException exception = assertThrows(MalpracticeException.class, () -> {
            paymentService.validatePayment(userId, paymentRequestDTO);
        });

        assertEquals("Order and Payment amounts different", exception.getMessage());
        verify(paymentsRepository, never()).save(any());
    }
}