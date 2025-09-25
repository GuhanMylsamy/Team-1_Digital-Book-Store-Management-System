package com.libraryManagement.project.dto.requestDTO;

import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull
    private String email;
    @NotNull
    private String fullName;
    @NotNull
    private String password;
    //private Role role;
}
