package com.libraryManagement.project.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private String type;
    private String identifier;
    private double amount;
    private int pin;
    private Long orderId;
}
