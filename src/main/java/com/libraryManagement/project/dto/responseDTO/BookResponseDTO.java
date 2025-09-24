package com.libraryManagement.project.dto.responseDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    private Long bookId;
    private String title;
    private String authorName;
    private String categoryName;
    private Double price;
    private int stockQuantity;
    private String imageUrl;

    public BookResponseDTO(Long bookId, String title, String authorName, String categoryName) {
        this.bookId = bookId;
        this.title = title;
        this.authorName = authorName;
        this.categoryName = categoryName;
    }

}
