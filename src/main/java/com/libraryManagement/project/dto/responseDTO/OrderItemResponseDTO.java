package com.libraryManagement.project.dto.responseDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long itemId;
    private String itemName;
    private int quantity;
    private double price;
    private boolean isReviewed;

}
