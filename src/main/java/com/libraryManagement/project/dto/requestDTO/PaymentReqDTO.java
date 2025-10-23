package com.libraryManagement.project.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentReqDTO {
    private String type;
    private String transactionId;
    private double amount;
    private Long orderId;
    private String status;
}
