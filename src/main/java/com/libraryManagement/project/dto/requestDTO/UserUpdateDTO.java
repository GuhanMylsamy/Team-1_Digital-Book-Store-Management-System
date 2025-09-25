package com.libraryManagement.project.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotBlank(message = "Name cannot be blank")
    private String fullName;

    @NotBlank(message = "password cannot be blank")
    @Min(6)
    private String oldPassword;

    @Min(6)
    @NotBlank(message = "Password cannot be blank")
    private String newPassword;

}
