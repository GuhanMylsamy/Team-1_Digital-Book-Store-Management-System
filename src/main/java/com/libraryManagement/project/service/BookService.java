package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.entity.Book;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface BookService {
    List<BookResponseDTO> getAllBooks();

    BookResponseDTO getBookById(Long id);

    List<BookResponseDTO> getBooksByAuthor(Long authorId);

    @Transactional
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);

    @Transactional
    BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO);

    @Transactional
    void deleteBook(Long id);

    default Book convertToEntity(BookRequestDTO bookRequestDTO) {
        return new Book(bookRequestDTO.getTitle(), null, null, bookRequestDTO.getPrice(),bookRequestDTO.getStockQuantity(),bookRequestDTO.getImageUrl());
    }

    default BookResponseDTO convertToResponseDTO(Book book) {
        return new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor().getName(), book.getCategory().getName(), book.getPrice(), book.getStockQuantity(),book.getImageUrl());
    }
}

