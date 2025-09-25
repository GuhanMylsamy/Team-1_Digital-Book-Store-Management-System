package com.libraryManagement.project.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "Old password must be at least 6 characters long")
    private String password;
}
