package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.ReviewRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository; // <-- Inject UserRepository

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Review> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @Override
    public Review createReview(Review review) {
        if(review.getRating()  < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating should be between 1 - 5");
        }
        if(review.getContent() == null) {
            throw new IllegalArgumentException("Comment cannot be Empty");
        }
        //SToring and saving contents here
        review.setContent(review.getContent());
        return reviewRepository.save(review);
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
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }
}
