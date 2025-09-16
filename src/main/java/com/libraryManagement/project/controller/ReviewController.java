package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.ReviewRequestDTO;
import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.impl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewService, BookRepository bookRepository, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    //Get all the reviews
    @GetMapping("/{bookId}")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviewsByBookId (@PathVariable Long bookId) {

        List<ReviewResponseDTO> reviews = reviewService.getAllReviews(bookId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("{bookId}/createReview")
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long bookId , @RequestBody ReviewRequestDTO reviewRequestDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found") );
        User user =  userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review newReview = new Review();
        newReview.setBook(book);
        newReview.setUser(user);
        newReview.setRating(reviewRequestDTO.getRating());
        newReview.setContent(reviewRequestDTO.getComment());

        ReviewResponseDTO reviewCreated = reviewService.createReview(newReview);
        return  new ResponseEntity<>(reviewCreated,HttpStatus.CREATED );
    }
}
