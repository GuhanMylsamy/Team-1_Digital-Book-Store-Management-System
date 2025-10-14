package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorAuthorId(Long authorId);

//    Optional<Book> findByTitle(String name);
    boolean existsByAuthor_AuthorId(Long authorId);
    boolean existsByCategory_CategoryId(Long categoryId);
    List<Book> findByCategoryCategoryId(Long categoryId);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorNameContainingIgnoreCase(String authorName);
    List<Book> findByCategoryNameContainingIgnoreCase(String categoryName);
    boolean existsByTitleAndAuthorAndCategory(String title, Author author, Category category);
}