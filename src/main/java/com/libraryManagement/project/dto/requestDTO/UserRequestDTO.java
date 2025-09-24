package com.libraryManagement.project.dto.requestDTO;

import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String email;
    private String fullName;
    private String password;

    //private Role role;
}
