package com.libraryManagement.project.dto.responseDTO;

import com.libraryManagement.project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDTO {

    private String token;
    private Role role;

    public UserAuthResponseDTO(String token, String name) {
    }
}
