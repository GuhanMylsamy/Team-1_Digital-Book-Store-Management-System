package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Review;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");

        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");

        review = new Review();
        review.setReviewId(1L);
        review.setBook(book);
        review.setUser(user);
        review.setRating(4.5);
        review.setContent("A great book!");
    }

    @Test
    void getAllReviews_shouldReturnListOfReviewResponseDTOs() {
        when(reviewRepository.findByBookId(anyLong())).thenReturn(Collections.singletonList(review));

        List<ReviewResponseDTO> result = reviewService.getAllReviews(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(reviewRepository, times(1)).findByBookId(1L);
    }

    @Test
    void createReview_shouldSaveAndReturnReviewResponseDTO() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewResponseDTO result = reviewService.createReview(review);

        assertNotNull(result);
        assertEquals(review.getReviewId(), result.getReviewId());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void createReview_shouldThrowException_whenRatingIsInvalid() {
        review.setRating(0); // Invalid rating
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(review);
        });
        assertEquals("Rating should be between 1 - 5", exception.getMessage());

        review.setRating(6); // Invalid rating
        exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(review);
        });
        assertEquals("Rating should be between 1 - 5", exception.getMessage());
    }
    @Test
    void createReview_shouldThrowException_whenContentIsNull() {
        review.setContent(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(review);
        });
        assertEquals("Comment cannot be Empty", exception.getMessage());
    }

    @Test
    void deleteReview_shouldReturnTrueWhenReviewExists() {
        when(reviewRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(anyLong());

        boolean result = reviewService.deleteReview(1L);

        assertTrue(result);
        verify(reviewRepository, times(1)).existsById(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReview_shouldReturnFalseWhenReviewDoesNotExist() {
        when(reviewRepository.existsById(anyLong())).thenReturn(false);

        boolean result = reviewService.deleteReview(1L);

        assertFalse(result);
        verify(reviewRepository, times(1)).existsById(1L);
        verify(reviewRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteReviewByAdmin_shouldDeleteReviewWhenReviewExists() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).delete(any(Review.class));

        reviewService.deleteReviewByAdmin(1L);

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteReviewByAdmin_shouldThrowResourceNotFoundExceptionWhenReviewDoesNotExist() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.deleteReviewByAdmin(1L);
        });

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, never()).delete(any(Review.class));
    }
}