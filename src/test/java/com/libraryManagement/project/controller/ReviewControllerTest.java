package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.ReviewRequestDTO;
import com.libraryManagement.project.dto.responseDTO.ReviewResponseDTO;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.security.JwtUtil; // Import JwtUtil
import com.libraryManagement.project.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // To bypass security checks
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    // FIX: Add a @MockBean for JwtUtil to satisfy the security filter dependency
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private User user;
    private ReviewRequestDTO reviewRequestDTO;
    private ReviewResponseDTO reviewResponseDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(1L);

        user = new User();
        user.setUserId(1L);

        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setComment("Great book!");
        reviewRequestDTO.setRating(5.0);

        reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setReviewId(1L);
        reviewResponseDTO.setBookId(1L);
        reviewResponseDTO.setComment("Great book!");
    }

    @Test
    @WithMockUser // Simulate an authenticated user
    void getAllReviewsByBookId_shouldReturnOkWithReviews() throws Exception {
        when(reviewService.getAllReviews(anyLong())).thenReturn(Collections.singletonList(reviewResponseDTO));

        mockMvc.perform(get("/api/v1/review/{bookId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Great book!"));
    }

    @Test
    @WithMockUser
    void createReview_shouldReturnCreated() throws Exception {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reviewService.createReview(any())).thenReturn(reviewResponseDTO);

        mockMvc.perform(post("/api/v1/review/createReview/{bookId}", 1L)
                        .with(csrf()) // Add CSRF token for POST requests
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(1L));
    }

    @Test
    @WithMockUser
    void createReview_shouldReturnNotFound_whenBookNotFound() throws Exception {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/review/createReview/{bookId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser
    void deleteReview_shouldReturnOkWhenDeleted() throws Exception {
        when(reviewService.deleteReview(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/review/delete/{reviewId}", 1L).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Review with ID 1 was deleted successfully."));
    }

    @Test
    @WithMockUser
    void deleteReview_shouldReturnNotFoundWhenNotDeleted() throws Exception {
        when(reviewService.deleteReview(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/v1/review/delete/{reviewId}", 1L).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Review with ID 1 not found."));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Test the PreAuthorize annotation
    void deleteReviewByAdmin_shouldReturnOk() throws Exception {
        doNothing().when(reviewService).deleteReviewByAdmin(anyLong());

        mockMvc.perform(delete("/api/v1/review/deleteByAdmin/{id}", 1L).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Review deleted successfully."));
    }
}