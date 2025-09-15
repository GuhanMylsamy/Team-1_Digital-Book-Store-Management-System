package com.libraryManagement.project.service;

import com.libraryManagement.project.entity.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();

    Author getAuthorById(Long id);

    Author getAuthorByName(String name);

    @Transactional
    Author addAuthor(Author author);

    @Transactional
    Author updateAuthor(Long id, Author authorDetails);

    @Transactional
    ResponseEntity<?> deleteAuthor(Long id);
}
