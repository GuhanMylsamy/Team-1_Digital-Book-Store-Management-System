package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.ReviewRequestDTO;
import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.service.ReviewService;
import com.libraryManagement.project.service.impl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    //Get all the reviews
    @GetMapping("/{bookId}")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviewsByBookId (@PathVariable Long bookId) {

        List<ReviewResponseDTO> reviews = reviewService.getAllReviews(bookId);
        return ResponseEntity.ok(reviews);
    }


}
