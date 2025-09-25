package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.entity.Category;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryServiceImpl categoryService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setName("Fiction");
    }

    @Test
    @WithMockUser
    void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(category));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fiction"));
    }

    @Test
    @WithMockUser
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testAddCategory() throws Exception {
        when(categoryService.addCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/categories/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateCategory() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/categories/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

}
