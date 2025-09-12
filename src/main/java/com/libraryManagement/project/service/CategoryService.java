package com.libraryManagement.project.service;
import com.libraryManagement.project.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    @Transactional
    Category addCategory(Category category);

    @Transactional
    Category updateCategory(Long id, Category categoryDetails);

    @Transactional
    ResponseEntity<?> deleteCategory(Long id);
}