package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.entity.Category;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private BookRepository bookRepository;

    @InjectMocks private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(new Category(1L, "Fiction")));
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Fiction", categories.get(0).getName());
    }

    @Test
    void testGetCategoryById_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Science")));
        Category category = categoryService.getCategoryById(1L);
        assertEquals("Science", category.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void testAddCategory_Success() {
        Category category = new Category(null, "History");
        when(categoryRepository.existsByName("History")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(new Category(1L, "History"));

        Category saved = categoryService.addCategory(category);
        assertEquals("History", saved.getName());
    }

    @Test
    void testAddCategory_AlreadyExists() {
        Category category = new Category(null, "History");
        when(categoryRepository.existsByName("History")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(category));
    }

    @Test
    void testUpdateCategory_Success() {
        Category existing = new Category(1L, "OldName");
        Category updatedDetails = new Category(null, "NewName");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(new Category(1L, "NewName"));

        Category updated = categoryService.updateCategory(1L, updatedDetails);
        assertEquals("NewName", updated.getName());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        Category updatedDetails = new Category(null, "NewName");
        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(99L, updatedDetails));
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByCategory_CategoryId(1L)).thenReturn(false);

        ResponseEntity<?> response = categoryService.deleteCategory(1L);
        assertEquals("Category deleted successfully!", response.getBody());
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(99L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(99L));
    }

    @Test
    void testDeleteCategory_UsedByBooks() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByCategory_CategoryId(1L)).thenReturn(true);

        ResponseEntity<?> response = categoryService.deleteCategory(1L);
        assertEquals("Category name is used by other books, Deletion is not supported.", response.getBody());
    }
}

