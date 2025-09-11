package com.libraryManagement.project.service;

import com.libraryManagement.project.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    // Retrieves all the reviews of any specific book
    List<Review> getReviewsForBook(Long bookId);

    //Create and save a new review
    Review createReview(Review review);

    //Delete Review
    boolean deleteReview(Long reviewId);

    //Search for any review by its reviewId
    Optional<Review> getReviewById(Long reviewId);



}
