package com.libraryManagement.project.dto.requestDTO;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String type;
    private String identifier;
    private double amount;
    private int pin;
    private Long orderId;
}
