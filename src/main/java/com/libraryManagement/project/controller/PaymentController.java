package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.PaymentRequestDTO;
import com.libraryManagement.project.dto.responseDTO.PaymentResponseDTO;
import com.libraryManagement.project.entity.Payments;
import com.libraryManagement.project.service.PaymentService;
import com.libraryManagement.project.service.impl.PaymentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class PaymentController {

    private PaymentServiceImpl paymentService;

    @PostMapping("/validatePayment")
    public ResponseEntity<PaymentResponseDTO> validatePayment(@RequestBody PaymentRequestDTO paymentRequestDTO, @RequestHeader Long userId){
        PaymentResponseDTO paymentResponseDTO = paymentService.validatePayment(userId,paymentRequestDTO);
        System.out.println(paymentRequestDTO);
        return new ResponseEntity<>(paymentResponseDTO, HttpStatus.OK);
    }
}
