package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.ReviewRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<ReviewResponseDTO> getAllReviews(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream()
                .map(ReviewResponseDTO::reviewResponseDTOMapper)
                .collect(Collectors.toList());
    }


    @Override
    public ReviewResponseDTO createReview(Review review) {
        if(review.getRating()  < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating should be between 1 - 5");
        }
        if(review.getContent() == null) {
            throw new IllegalArgumentException("Comment cannot be Empty");
        }
        //SToring and saving contents here
        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDTO.reviewResponseDTOMapper(savedReview);

    }

    @Override
    public boolean deleteReview(Long reviewId) {
        if(reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    @Override
    public void deleteReviewByAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found!"));
        reviewRepository.delete(review);
    }

//    @Override
//    public Optional<Review> getReviewById(Long reviewId) {
//        return reviewRepository.findById(reviewId);
//    }


}
