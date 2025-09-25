package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private BookResponseDTO bookResponseDTO;
    private BookRequestDTO bookRequestDTO;

    @BeforeEach
    void setUp() {
        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setBookId(1L);
        bookResponseDTO.setTitle("1984");
        bookResponseDTO.setAuthorName("George Orwell");
        bookResponseDTO.setCategoryName("Dystopian");
        bookResponseDTO.setPrice(299.99);

        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("1984");
        bookRequestDTO.setAuthorName("Thomas");
        bookRequestDTO.setCategoryName("Fiction");
        bookRequestDTO.setPrice(299.99);
    }

    @Test
    @WithMockUser
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(bookResponseDTO));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("1984"));
    }

    @Test
    @WithMockUser
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookResponseDTO);

        mockMvc.perform(get("/api/v1/books/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetBooksByAuthorId() throws Exception {
        when(bookService.getBooksByAuthor(1L)).thenReturn(Collections.singletonList(bookResponseDTO));

        mockMvc.perform(get("/api/v1/books/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName").value("George Orwell"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetBooksByCategoryId() throws Exception {
        when(bookService.getBooksByCategory(1L)).thenReturn(Collections.singletonList(bookResponseDTO));

        mockMvc.perform(get("/api/v1/books/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Dystopian"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testAddBook() throws Exception {
        when(bookService.addBook(any(BookRequestDTO.class))).thenReturn(bookResponseDTO);

        mockMvc.perform(post("/api/v1/books/addBook")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(anyLong(), any(BookRequestDTO.class))).thenReturn(bookResponseDTO);

        mockMvc.perform(put("/api/v1/books/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    @WithMockUser
    void testFindBooksByTitle() throws Exception {
        when(bookService.findBooksByTitle("1984")).thenReturn(Collections.singletonList(bookResponseDTO));

        mockMvc.perform(get("/api/v1/books/findByTitle")
                        .param("title", "1984"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("1984"));
    }

    @Test
    @WithMockUser
    void testFindBooksByAuthorName() throws Exception {
        when(bookService.findBooksByAuthor("George Orwell")).thenReturn(Collections.singletonList(bookResponseDTO));

        mockMvc.perform(get("/api/v1/books/findByAuthor")
                        .param("authorName", "George Orwell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName").value("George Orwell"));
    }

    @Test
    @WithMockUser
    void testFindBooksByCategoryName() throws Exception {
        when(bookService.findBooksByCategory("Dystopian")).thenReturn(Collections.singletonList(bookResponseDTO));

        mockMvc.perform(get("/api/v1/books/findByCategory")
                        .param("categoryName", "Dystopian"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Dystopian"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/v1/books/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message").value("Book deleted successfully"));
    }
}
