package com.libraryManagement.project.dto.responseDTO;
import com.libraryManagement.project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String email;
    private String fullName;
    private Role role;

}
