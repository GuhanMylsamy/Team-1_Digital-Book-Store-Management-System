package com.libraryManagement.project.dto.requestDTO;

import lombok.Data;

@Data
public class UserUpdateDTO {

    private String fullName;

    private String oldPassword;

    private String newPassword;

}
