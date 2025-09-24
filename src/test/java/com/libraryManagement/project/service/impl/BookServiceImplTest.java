package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.entity.*;
import com.libraryManagement.project.exception.BookNotFoundException;
import com.libraryManagement.project.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock private BookRepository bookRepository;
    @Mock private AuthorRepository authorRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private CartItemsRepository cartItemsRepository;
    @Mock private OrderItemsRepository orderItemsRepository;
    @Mock private CartRepository cartRepository;

    @InjectMocks private BookServiceImpl bookService;

    // Helper method to create a Book
    private Book createBook() {
        Author author = new Author(1L, "Author Name");
        Category category = new Category(1L, "Category Name");
        return new Book(1L, "Title", author, category, 100.0, 10, true, "image.jpg");
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(createBook()));
        List<BookResponseDTO> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Title", books.get(0).getTitle());
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(createBook()));
        BookResponseDTO book = bookService.getBookById(1L);
        assertEquals("Title", book.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void testGetBooksByAuthor() {
        when(bookRepository.findByAuthorAuthorId(1L)).thenReturn(List.of(createBook()));
        List<BookResponseDTO> books = bookService.getBooksByAuthor(1L);
        assertEquals(1, books.size());
    }

    @Test
    void testGetBooksByCategory() {
        when(bookRepository.findByCategoryCategoryId(1L)).thenReturn(List.of(createBook()));
        List<BookResponseDTO> books = bookService.getBooksByCategory(1L);
        assertEquals(1, books.size());
    }

    @Test
    void testFindBooksByTitle() {
        when(bookRepository.findByTitleContainingIgnoreCase("Title")).thenReturn(List.of(createBook()));
        List<BookResponseDTO> books = bookService.findBooksByTitle("Title");
        assertEquals(1, books.size());
    }

    @Test
    void testFindBooksByAuthorName() {
        when(bookRepository.findByAuthorNameContainingIgnoreCase("Author")).thenReturn(List.of(createBook()));
        List<BookResponseDTO> books = bookService.findBooksByAuthor("Author");
        assertEquals(1, books.size());
    }

    @Test
    void testFindBooksByCategoryName() {
        when(bookRepository.findByCategoryNameContainingIgnoreCase("Category")).thenReturn(List.of(createBook()));
        List<BookResponseDTO> books = bookService.findBooksByCategory("Category");
        assertEquals(1, books.size());
    }

    @Test
    void testAddBook_Success() {
        BookRequestDTO request = new BookRequestDTO("Title", "Author Name", "Category Name", 100.0, 10, "image.jpg");

        Author author = new Author(1L, "Author Name");
        Category category = new Category(1L, "Category Name");

        when(authorRepository.findByName("Author Name")).thenReturn(Optional.of(author));
        when(categoryRepository.findByName("Category Name")).thenReturn(Optional.of(category));
        when(bookRepository.existsByTitleAndAuthorAndCategory("Title", author, category)).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(new Inventory());

        BookResponseDTO response = bookService.addBook(request);
        assertEquals("Title", response.getTitle());
    }

    @Test
    void testAddBook_Duplicate() {
        BookRequestDTO request = new BookRequestDTO("Title", "Author Name", "Category Name", 100.0, 10, "image.jpg");
        Author author = new Author(1L, "Author Name");
        Category category = new Category(1L, "Category Name");

        when(authorRepository.findByName("Author Name")).thenReturn(Optional.of(author));
        when(categoryRepository.findByName("Category Name")).thenReturn(Optional.of(category));
        when(bookRepository.existsByTitleAndAuthorAndCategory("Title", author, category)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(request));
    }

    @Test
    void testUpdateBook_Success() {
        Book existingBook = createBook();
        BookRequestDTO request = new BookRequestDTO("Updated", "Author Name", "Category Name", 120.0, 5, "new.jpg");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findByName("Author Name")).thenReturn(Optional.of(existingBook.getAuthor()));
        when(categoryRepository.findByName("Category Name")).thenReturn(Optional.of(existingBook.getCategory()));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryRepository.findByBook(existingBook)).thenReturn(Optional.of(new Inventory()));

        BookResponseDTO updated = bookService.updateBook(1L, request);
        assertEquals("Updated", updated.getTitle());
        assertEquals(120.0, updated.getPrice());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        BookRequestDTO request = new BookRequestDTO("Title", "Author", "Category", 100.0, 10, "img.jpg");
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(99L, request));
    }

    @Test
    void testDeleteBook_NotInOrders() {
        Book book = createBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(orderItemsRepository.existsByBookId(1L)).thenReturn(false);
        when(cartItemsRepository.findBookByBookId(1L)).thenReturn(List.of());

        bookService.deleteBook(1L);
        verify(bookRepository).delete(book);
    }

    @Test
    void testDeleteBook_InOrders() {
        Book book = createBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(orderItemsRepository.existsByBookId(1L)).thenReturn(true);

        bookService.deleteBook(1L);
        assertFalse(book.getActive());
        verify(bookRepository).save(book);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(99L));
    }
}

