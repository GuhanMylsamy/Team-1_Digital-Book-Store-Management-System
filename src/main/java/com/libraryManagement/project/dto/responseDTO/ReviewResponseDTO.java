package com.libraryManagement.project.dto.responseDTO;


import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long reviewId;

    private String title;

    private String reviewerName;

    private String comment;

    private Double rating;

    private Long bookId;

    public static ReviewResponseDTO reviewResponseDTOMapper(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(review.getReviewId());
        dto.setTitle(review.getBook().getTitle());
        dto.setReviewerName(review.getUser().getFullName());
        dto.setComment(review.getContent());
        dto.setRating(review.getRating());
        dto.setBookId( review.getBook().getBookId());
        return dto;
    }
}
