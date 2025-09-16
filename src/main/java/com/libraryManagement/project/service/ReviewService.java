package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Review;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    //List<ReviewResponseDTO> getAllReviews(Book bookId);

    // Retrieves all the reviews of any specific book
    List<ReviewResponseDTO> getAllReviews(Long bookId);

    //Create and save a new review
    @Transactional
    ReviewResponseDTO createReview(Review review);

    //Delete Review
    boolean deleteReview(Long reviewId);

    //Search for any review by its reviewId
    Optional<Review> getReviewById(Long reviewId);


}
