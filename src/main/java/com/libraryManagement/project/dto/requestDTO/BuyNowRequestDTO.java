package com.libraryManagement.project.dto.requestDTO;

import lombok.Data;

@Data
public class BuyNowRequestDTO {

    private Long userId;
    private Long bookId;
    private Long addressId;
    private int quantity;


}
