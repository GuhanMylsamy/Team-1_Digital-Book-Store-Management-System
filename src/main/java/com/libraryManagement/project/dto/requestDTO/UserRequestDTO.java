package com.libraryManagement.project.dto.requestDTO;

import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank
    private String email;
    @NotBlank(message = "Name cannot be blank")

    private String fullName;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    //private Role role;
}
