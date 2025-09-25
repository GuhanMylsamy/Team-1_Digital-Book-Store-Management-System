package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private JwtUtil jwtUtil; // Mocked to satisfy security filter dependencies

    @Autowired
    private ObjectMapper objectMapper;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setAuthorId(1L);
        author.setName("George Orwell");
    }

    @Test
    @WithMockUser
    void testGetAllAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("George Orwell"));
    }

    @Test
    @WithMockUser
    void testGetAuthorById() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(author);

        mockMvc.perform(get("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("George Orwell"));
    }

    @Test
    @WithMockUser
    void testGetAuthorByName() throws Exception {
        when(authorService.getAuthorByName("George Orwell")).thenReturn(author);

        mockMvc.perform(get("/api/v1/authors/getAuthor")
                        .param("name", "George Orwell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("George Orwell"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testAddAuthor() throws Exception {
        when(authorService.addAuthor(any(Author.class))).thenReturn(author);

        mockMvc.perform(post("/api/v1/authors/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("George Orwell"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateAuthor() throws Exception {
        when(authorService.updateAuthor(eq(1L), any(Author.class))).thenReturn(author);

        mockMvc.perform(put("/api/v1/authors/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("George Orwell"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteAuthor() throws Exception {
        when(authorService.deleteAuthor(1L)).thenReturn(
                org.springframework.http.ResponseEntity.ok("Author deleted successfully!")
        );

        mockMvc.perform(delete("/api/v1/authors/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Author deleted successfully!"));
    }
}
