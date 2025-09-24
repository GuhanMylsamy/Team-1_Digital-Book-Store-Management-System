package com.libraryManagement.project.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyNowRequestDTO {

    private Long userId;
    private Long bookId;
    private Long addressId;
    private int quantity;


    public BuyNowRequestDTO(long l, long l1, long l2, int i) {
    }
}
