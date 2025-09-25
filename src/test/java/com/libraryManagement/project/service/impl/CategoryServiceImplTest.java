package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.entity.Category;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    private CategoryRepository categoryRepository;
    private BookRepository bookRepository;
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        bookRepository = mock(BookRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository, bookRepository);

        category = new Category();
        category.setCategoryId(1L);
        category.setName("Fiction");
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<Category> result = categoryService.getAllCategories();
        assertEquals(1, result.size());
        assertEquals("Fiction", result.get(0).getName());
    }

    @Test
    void testGetCategoryById_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1L);
        assertEquals("Fiction", result.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(2L));
    }

    @Test
    void testAddCategory_Success() {
        when(categoryRepository.existsByName("Fiction")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.addCategory(category);
        assertEquals("Fiction", result.getName());
    }

    @Test
    void testAddCategory_AlreadyExists() {
        when(categoryRepository.existsByName("Fiction")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(category));
    }

    @Test
    void testUpdateCategory_Success() {
        Category updated = new Category();
        updated.setName("Drama");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);

        Category result = categoryService.updateCategory(1L, updated);
        assertEquals("Drama", result.getName());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Category updated = new Category();
        updated.setName("Drama");

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(2L, updated));
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByCategory_CategoryId(1L)).thenReturn(false);

        ResponseEntity<?> response = categoryService.deleteCategory(1L);
        assertEquals("Category deleted successfully!", response.getBody());
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(2L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(2L));
    }

    @Test
    void testDeleteCategory_UsedByBooks() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByCategory_CategoryId(1L)).thenReturn(true);

        ResponseEntity<?> response = categoryService.deleteCategory(1L);
        assertEquals("Category name is used by other books, Deletion is not supported.", response.getBody());
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
