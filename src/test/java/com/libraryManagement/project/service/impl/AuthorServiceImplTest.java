package com.libraryManagement.project.service.impl;

// Imports
import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.AuthorRepository;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void testGetAllAuthors() {
        Mockito.when(authorRepository.findAll()).thenReturn(List.of(new Author(1L, "John")));
        List<Author> authors = authorService.getAllAuthors();
        assertEquals(1, authors.size());
    }

    @Test
    void testGetAuthorById_Found() {
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author(1L, "Jane")));
        Author author = authorService.getAuthorById(1L);
        assertEquals("Jane", author.getName());
    }

    @Test
    void testGetAuthorById_NotFound() {
        Mockito.when(authorRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(2L));
    }

    @Test
    void testGetAuthorByName_Found() {
        Mockito.when(authorRepository.findByName("Alice")).thenReturn(Optional.of(new Author(2L, "Alice")));
        Author author = authorService.getAuthorByName("Alice");
        assertEquals("Alice", author.getName());
    }

    @Test
    void testGetAuthorByName_NotFound() {
        Mockito.when(authorRepository.findByName("Bob")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorByName("Bob"));
    }

    @Test
    void testAddAuthor_Success() {
        Author author = new Author(null, "NewAuthor");
        Mockito.when(authorRepository.existsByName("NewAuthor")).thenReturn(false);
        Mockito.when(authorRepository.save(author)).thenReturn(new Author(3L, "NewAuthor"));

        Author saved = authorService.addAuthor(author);
        assertEquals("NewAuthor", saved.getName());
    }

    @Test
    void testAddAuthor_AlreadyExists() {
        Author author = new Author(null, "ExistingAuthor");
        Mockito.when(authorRepository.existsByName("ExistingAuthor")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authorService.addAuthor(author));
    }

    @Test
    void testUpdateAuthor_Success() {
        Author existing = new Author(1L, "OldName");
        Author updatedDetails = new Author(null, "NewName");

        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(authorRepository.save(existing)).thenReturn(new Author(1L, "NewName"));

        Author updated = authorService.updateAuthor(1L, updatedDetails);
        assertEquals("NewName", updated.getName());
    }

    @Test
    void testUpdateAuthor_NotFound() {
        Mockito.when(authorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> authorService.updateAuthor(99L, new Author(null, "Name")));
    }

    @Test
    void testDeleteAuthor_Success() {
        Mockito.when(authorRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.existsByAuthor_AuthorId(1L)).thenReturn(false);

        ResponseEntity<String> response = authorService.deleteAuthor(1L);
        assertEquals("Author deleted successfully!", response.getBody());
    }

    @Test
    void testDeleteAuthor_NotFound() {
        Mockito.when(authorRepository.existsById(2L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> authorService.deleteAuthor(2L));
    }

    @Test
    void testDeleteAuthor_UsedByBooks() {
        Mockito.when(authorRepository.existsById(3L)).thenReturn(true);
        Mockito.when(bookRepository.existsByAuthor_AuthorId(3L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(3L));
    }
}

