package com.libraryManagement.project.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotBlank(message = "Name cannot be blank")
    private String fullName;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, message = "Old password must be at least 6 characters long")
    private String oldPassword;


    @Size(min = 6, message = "New password must be at least 6 characters long")
    private String newPassword;

}
