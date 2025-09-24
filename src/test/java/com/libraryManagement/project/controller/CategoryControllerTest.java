package com.libraryManagement.project.controller;

import com.libraryManagement.project.entity.Category;
import com.libraryManagement.project.service.impl.CategoryServiceImpl;
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

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(new Category(1L, "Fiction")));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fiction"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Mockito.when(categoryService.getCategoryById(1L)).thenReturn(new Category(1L, "Science"));

        mockMvc.perform(get("/categories/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Science"));
    }

    @Test
    void testAddCategory() throws Exception {
        Category category = new Category(2L, "History");
        Mockito.when(categoryService.addCategory(Mockito.any())).thenReturn(category);

        mockMvc.perform(post("/categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"History\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("History"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category updated = new Category(1L, "UpdatedCategory");
        Mockito.when(categoryService.updateCategory(Mockito.eq(1L), Mockito.any())).thenReturn(updated);

        mockMvc.perform(put("/categories/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedCategory\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedCategory"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Mockito.when(categoryService.deleteCategory(1L)).thenReturn(ResponseEntity.ok("Category deleted"));

        mockMvc.perform(delete("/categories/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted"));
    }
}

