package com.libraryManagement.project.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookRequestDTO {
    @NotNull(message= "Title cannot be null")
    private String title;

    @NotNull(message = "Author Name cannot be null")
    private String authorName;

    @NotNull(message = "Category Name cannot be null")
    private String categoryName;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    private int stockQuantity;
    private String imageUrl;

}
