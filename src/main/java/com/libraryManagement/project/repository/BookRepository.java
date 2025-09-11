package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorAuthorId(Long authorId);

    boolean existsByTitleAndAuthorAndCategory(String title, Author author, Category category);
}