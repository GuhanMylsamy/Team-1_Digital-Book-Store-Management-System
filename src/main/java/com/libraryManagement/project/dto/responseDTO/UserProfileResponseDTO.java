package com.libraryManagement.project.dto.responseDTO;

import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.ShippingAddress;
import com.libraryManagement.project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {

    private Long userId;
    private String fullName;
    private String email;
    private List<Order> orders;
    private List<ShippingAddress> addresses;
    private Role role;
}
