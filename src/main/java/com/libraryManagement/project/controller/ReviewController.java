package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.ReviewRequestDTO;
import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.exception.UserNotFoundException;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.impl.ReviewServiceImpl;
import com.libraryManagement.project.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/review")
@CrossOrigin(origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

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

    @PostMapping("createReview/{bookId}")
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long bookId , @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found") );
        User user =  userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Review newReview = new Review();
        newReview.setBook(book);
        newReview.setUser(user);
        newReview.setRating(reviewRequestDTO.getRating());
        newReview.setContent(reviewRequestDTO.getComment());

        ReviewResponseDTO reviewCreated = reviewService.createReview(newReview);
        return  new ResponseEntity<>(reviewCreated,HttpStatus.CREATED );
    }


    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long reviewId) {
        boolean wasDeleted = reviewService.deleteReview(reviewId);
        if (wasDeleted) {
            Map<String, String> responseBody = Map.of(
                    "status", "success",
                    "message", "Review with ID " + reviewId + " was deleted successfully."
            );
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, String> errorBody = Map.of(
                    "status", "error",
                    "message", "Review with ID " + reviewId + " not found."
            );
            return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteByAdmin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteReviewByAdmin(@PathVariable Long id) {
        reviewService.deleteReviewByAdmin(id);
        return ResponseEntity.ok("Review deleted successfully.");
    }



}
