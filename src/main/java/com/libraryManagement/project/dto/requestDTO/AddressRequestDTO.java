package com.libraryManagement.project.dto.requestDTO;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressRequestDTO {
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String addressLine1;
    private String addressLine2; // This can be optional
    @NotEmpty
    private String city;
    @NotEmpty
    private String state;
    @NotEmpty
    private String postalCode;
    @NotEmpty
    private String country;

}