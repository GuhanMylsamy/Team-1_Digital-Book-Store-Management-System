package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.PaymentRequestDTO;
import com.libraryManagement.project.dto.responseDTO.PaymentResponseDTO;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentServiceImpl paymentService;

    // Mock JwtUtil to satisfy security dependency
    @MockBean
    private JwtUtil jwtUtil;

    private PaymentResponseDTO paymentResponseDTO;
    private PaymentRequestDTO paymentRequestDTO;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        paymentRequestDTO = new PaymentRequestDTO("UPI", "test@upi", 150.0, 1234, 1L);
        paymentResponseDTO = PaymentResponseDTO.builder()
                .paymentId(1L)
                .status("SUCCESS")
                .transactionId("Tnx_xyz")
                .build();
    }

    @Test
    @WithMockUser
    void testValidatePayment_Success() throws Exception {
        // Arrange
        given(paymentService.validatePayment(anyLong(), any(PaymentRequestDTO.class))).willReturn(paymentResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/payment/validatePayment")
                        .with(csrf()) // Add CSRF token for POST request
                        .header("userId", userId) // Pass userId as a request header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.transactionId").value("Tnx_xyz"));
    }
}