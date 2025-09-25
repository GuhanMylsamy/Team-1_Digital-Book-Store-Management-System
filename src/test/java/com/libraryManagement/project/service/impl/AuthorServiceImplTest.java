package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.AuthorRepository;
import com.libraryManagement.project.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceImplTest {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private AuthorServiceImpl authorService;

    private Author author;

    @BeforeEach
    public void setup() {
        authorRepository = mock(AuthorRepository.class);
        bookRepository = mock(BookRepository.class);
        authorService = new AuthorServiceImpl(authorRepository, bookRepository);

        author = new Author();
        author.setAuthorId(1L);
        author.setName("George Orwell");
    }

    @Test
    public void testGetAllAuthors() {
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));

        List<Author> authors = authorService.getAllAuthors();
        assertEquals(1, authors.size());
        assertEquals("George Orwell", authors.get(0).getName());
    }

    @Test
    public void testGetAuthorById_Found() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.getAuthorById(1L);
        assertEquals("George Orwell", result.getName());
    }

    @Test
    public void testGetAuthorById_NotFound() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(2L));
    }

    @Test
    public void testGetAuthorByName_Found() {
        when(authorRepository.findByName("George Orwell")).thenReturn(Optional.of(author));

        Author result = authorService.getAuthorByName("George Orwell");
        assertEquals(1L, result.getAuthorId());
    }

    @Test
    public void testGetAuthorByName_NotFound() {
        when(authorRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorByName("Unknown"));
    }

    @Test
    public void testAddAuthor_Success() {
        when(authorRepository.existsByName("George Orwell")).thenReturn(false);
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.addAuthor(author);
        assertEquals("George Orwell", result.getName());
    }

    @Test
    public void testAddAuthor_AlreadyExists() {
        when(authorRepository.existsByName("George Orwell")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authorService.addAuthor(author));
    }

    @Test
    public void testUpdateAuthor_Success() {
        Author updated = new Author();
        updated.setName("Eric Blair");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(updated);

        Author result = authorService.updateAuthor(1L, updated);
        assertEquals("Eric Blair", result.getName());
    }

    @Test
    public void testUpdateAuthor_NotFound() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        Author updated = new Author();
        updated.setName("New Name");

        assertThrows(IllegalArgumentException.class, () -> authorService.updateAuthor(2L, updated));
    }

    @Test
    public void testDeleteAuthor_Success() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByAuthor_AuthorId(1L)).thenReturn(false);

        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAuthor_NotFound() {
        when(authorRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authorService.deleteAuthor(2L));
    }

    @Test
    public void testDeleteAuthor_UsedByBooks() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByAuthor_AuthorId(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(1L));
    }
}
