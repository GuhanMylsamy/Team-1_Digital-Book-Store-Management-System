package com.libraryManagement.project.dto.responseDTO;

import com.libraryManagement.project.entity.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDTO {
    private Long paymentId;
    private String transactionId;
    private String status;
    private Order order;

}
