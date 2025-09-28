package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.constants.PaymentConstants;
import com.libraryManagement.project.dto.requestDTO.PaymentRequestDTO;
import com.libraryManagement.project.dto.responseDTO.PaymentResponseDTO;
import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.Payments;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.PaymentStatus;
import com.libraryManagement.project.exception.MalpracticeException;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.OrdersRepository;
import com.libraryManagement.project.repository.PaymentsRepository;
import com.libraryManagement.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl {

    private UserRepository userRepository;
    private OrdersRepository ordersRepository;
    private PaymentsRepository paymentsRepository;

    public PaymentResponseDTO validatePayment(Long userId, PaymentRequestDTO paymentRequestDTO){
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("No user exists");
        }

        Order order = ordersRepository.findById(paymentRequestDTO.getOrderId()).orElseThrow(
                () -> new ResourceNotFoundException("No order found"));

        if(order.getTotalAmount() != paymentRequestDTO.getAmount()){
            throw new MalpracticeException("Order and Payment amounts different");
        }

        boolean isPaymentSuccess = PaymentConstants.validatePayment(paymentRequestDTO.getType(),paymentRequestDTO.getIdentifier(),paymentRequestDTO.getPin(),paymentRequestDTO.getAmount());

        Payments payment = new Payments();
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setType(paymentRequestDTO.getType());
        payment.setIdentifier(paymentRequestDTO.getIdentifier());
        payment.setTransactionId("Tnx_" + UUID.randomUUID());
        payment.setOrder(order);

        if(isPaymentSuccess){
            payment.setStatus(String.valueOf(PaymentStatus.SUCCESS));
        }
        else{
            payment.setStatus(String.valueOf(PaymentStatus.FAILED));
        }

        Payments savedPayment = paymentsRepository.save(payment);

        return PaymentResponseDTO.builder()
                .paymentId(savedPayment.getPaymentId())
                .status(savedPayment.getStatus())
                .transactionId(savedPayment.getTransactionId())
                .order(savedPayment.getOrder())
                .build();


    }

}
