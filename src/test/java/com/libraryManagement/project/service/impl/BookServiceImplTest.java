package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.entity.*;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private InventoryRepository inventoryRepository;
    private CartItemsRepository cartItemsRepository;
    private OrderItemsRepository orderItemsRepository;
    private CartRepository cartRepository;

    private BookServiceImpl bookService;

    private Book book;
    private BookRequestDTO bookRequestDTO;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        authorRepository = mock(AuthorRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        inventoryRepository = mock(InventoryRepository.class);
        cartItemsRepository = mock(CartItemsRepository.class);
        orderItemsRepository = mock(OrderItemsRepository.class);
        cartRepository = mock(CartRepository.class);

        bookService = new BookServiceImpl(bookRepository, authorRepository, categoryRepository,
                inventoryRepository, cartItemsRepository, orderItemsRepository, cartRepository);

        book = new Book();
        book.setBookId(1L);
        book.setTitle("1984");
        book.setPrice(299.99);
        book.setStockQuantity(10);
        book.setActive(true);

        bookRequestDTO = new BookRequestDTO("1984", "George Orwell", "Dystopian", 299.99, 10, null);
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponseDTO result = bookService.getBookById(1L);
        assertEquals("1984", result.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2L));
    }

    @Test
    void testAddBook_NewAuthorAndCategory() {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setName("George Orwell");

        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Dystopian");

        when(authorRepository.findByName("George Orwell")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        when(categoryRepository.findByName("Dystopian")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        when(bookRepository.existsByTitleAndAuthorAndCategory(anyString(), any(), any())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO result = bookService.addBook(bookRequestDTO);
        assertEquals("1984", result.getTitle());
    }

    @Test
    void testAddBook_DuplicateBook() {
        Author author = new Author();
        Category category = new Category();

        when(authorRepository.findByName(anyString())).thenReturn(Optional.of(author));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(bookRepository.existsByTitleAndAuthorAndCategory(anyString(), any(), any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(bookRequestDTO));
    }

    @Test
    void testUpdateBook_Success() {
        Author author = new Author();
        Category category = new Category();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findByName(anyString())).thenReturn(Optional.of(author));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO result = bookService.updateBook(1L, bookRequestDTO);
        assertEquals("1984", result.getTitle());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(2L, bookRequestDTO));
    }

    @Test
    void testDeleteBook_NotInOrders() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(orderItemsRepository.existsByBookId(1L)).thenReturn(false);
        when(cartItemsRepository.findBookByBookId(1L)).thenReturn(new ArrayList<>());

        bookService.deleteBook(1L);
        verify(bookRepository).delete(book);
    }

    @Test
    void testDeleteBook_InOrders() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(orderItemsRepository.existsByBookId(1L)).thenReturn(true);

        bookService.deleteBook(1L);
        verify(bookRepository).save(book);
        assertFalse(book.getActive());
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(2L));
    }
}
