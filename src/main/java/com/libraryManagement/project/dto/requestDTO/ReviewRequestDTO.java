package com.libraryManagement.project.dto.requestDTO;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class ReviewRequestDTO {

    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 10 , message = "Comment should be at least 10 characters long")
    private String comment;

    @Min(value = 1 , message = "Rating cannot be below 1.0")
    @Max(value = 5 , message = "Rating cannot be above 5.0")
    private double rating;


}
