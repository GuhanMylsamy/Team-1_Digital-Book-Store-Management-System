package com.libraryManagement.project.controller;

import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Endpoint for creating NEW REVIEW
    @PostMapping("/reviews")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        try {
            Review newReview = reviewService.createReview(review);
            return new ResponseEntity<>(newReview , HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
