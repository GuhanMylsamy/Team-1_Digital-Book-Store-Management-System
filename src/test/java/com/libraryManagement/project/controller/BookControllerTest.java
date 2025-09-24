package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @Test
    void testGetAllBooks() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void testGetBookById() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testGetBooksByAuthorId() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.getBooksByAuthor(1L)).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Author"));
    }

    @Test
    void testGetBooksByCategoryId() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.getBooksByCategory(1L)).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Category"));
    }

    @Test
    void testAddBook() throws Exception {
        BookRequestDTO request = new BookRequestDTO("Title", "Author", "Category");
        BookResponseDTO response = new BookResponseDTO(1L, "Title", "Author", "Category");

        Mockito.when(bookService.addBook(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/books/addBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\",\"authorName\":\"Author\",\"categoryName\":\"Category\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookResponseDTO response = new BookResponseDTO(1L, "Updated", "Author", "Category");

        Mockito.when(bookService.updateBook(Mockito.eq(1L), Mockito.any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/books/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated\",\"authorName\":\"Author\",\"categoryName\":\"Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void testFindBooksByTitle() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.findBooksByTitle("Title")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books/findByTitle").param("title", "Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void testFindBooksByAuthorName() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.findBooksByAuthor("Author")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books/findByAuthor").param("authorName", "Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Author"));
    }

    @Test
    void testFindBooksByCategoryName() throws Exception {
        BookResponseDTO book = new BookResponseDTO(1L, "Title", "Author", "Category");
        Mockito.when(bookService.findBooksByCategory("Category")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books/findByCategory").param("categoryName", "Category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Category"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/v1/books/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message").value("Book deleted successfully"));
    }
}
