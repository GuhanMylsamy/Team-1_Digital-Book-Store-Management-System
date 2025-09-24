package com.libraryManagement.project.controller;

import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Test
    void testGetAllAuthors() throws Exception {
        Mockito.when(authorService.getAllAuthors()).thenReturn(List.of(new Author(1L, "John")));

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void testGetAuthorById() throws Exception {
        Mockito.when(authorService.getAuthorById(1L)).thenReturn(new Author(1L, "Jane"));

        mockMvc.perform(get("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"));
    }

    @Test
    void testGetAuthorByName() throws Exception {
        Mockito.when(authorService.getAuthorByName("Alice")).thenReturn(new Author(2L, "Alice"));

        mockMvc.perform(get("/api/v1/authors/getAuthor").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testAddAuthor() throws Exception {
        Author author = new Author(3L, "Mark");
        Mockito.when(authorService.addAuthor(Mockito.any())).thenReturn(author);

        mockMvc.perform(post("/api/v1/authors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mark\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mark"));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        Author updated = new Author(1L, "UpdatedName");
        Mockito.when(authorService.updateAuthor(Mockito.eq(1L), Mockito.any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/authors/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        Mockito.when(authorService.deleteAuthor(1L)).thenReturn(ResponseEntity.ok("Author deleted successfully!"));

        mockMvc.perform(delete("/api/v1/authors/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Author deleted successfully!"));
    }
}

