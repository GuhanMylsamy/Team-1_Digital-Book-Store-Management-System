package com.libraryManagement.project.dto.responseDTO;

import com.libraryManagement.project.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private String transactionId;
    private String status;
    private Order order;

}
