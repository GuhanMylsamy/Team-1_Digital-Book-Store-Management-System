package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.PaymentRequestDTO;
import com.libraryManagement.project.dto.responseDTO.PaymentResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


public interface PaymentService {

    @Transactional
    PaymentResponseDTO validatePayment(Long userId, PaymentRequestDTO paymentRequestDTO);
}
