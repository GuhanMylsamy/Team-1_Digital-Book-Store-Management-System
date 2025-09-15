package com.libraryManagement.project.service.impl;
import com.libraryManagement.project.entity.Category;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.repository.CategoryRepository;
import com.libraryManagement.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository=bookRepository;
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found!"));
    }

    @Transactional
    @Override
    public Category addCategory(Category category) {

        boolean exists = categoryRepository.existsByName(category.getName());

        if (exists) {
            throw new IllegalArgumentException("Category already exists in the database.");
        }

        return categoryRepository.save(category);
    }
    @Transactional
    @Override
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found!"));
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }
    @Transactional
    @Override
    public ResponseEntity<?> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found!");
        }
        else if (bookRepository.existsByCategory_CategoryId(id)) {
            return ResponseEntity.badRequest().body(new IllegalArgumentException("Category name is used by other books, Deletion is not supported.").getMessage());
        }
        else {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok("Category deleted successfully!");
        }
    }
}
