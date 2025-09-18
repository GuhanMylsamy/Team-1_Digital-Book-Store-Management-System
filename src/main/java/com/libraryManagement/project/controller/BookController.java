package com.libraryManagement.project.controller;


import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.service.impl.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookServiceImpl bookService;

    //constructor injection
    @Autowired
    public BookController(BookServiceImpl bookService){
        this.bookService = bookService;
    }

    //Get request for fetching all the books
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    //Get request for fetching all the books by Id
    @GetMapping("/get/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponseDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookResponseDTO>> getBooksByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId));
    }

    @PostMapping("/addBook")
    public ResponseEntity<BookResponseDTO> addBook(@RequestBody BookRequestDTO bookRequestDTO){
        BookResponseDTO createdBook = bookService.addBook(bookRequestDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id,@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO updatedBook = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<List<BookResponseDTO>> findBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.findBooksByTitle(title));
    }

    @GetMapping("/findByAuthor")
    public ResponseEntity<List<BookResponseDTO>> findBooksByAuthor(@RequestParam String authorName) {
        return ResponseEntity.ok(bookService.findBooksByAuthor(authorName));
    }

    @GetMapping("/findByCategory")
    public ResponseEntity<List<BookResponseDTO>> findBooksByCategory(@RequestParam String categoryName) {
        return ResponseEntity.ok(bookService.findBooksByCategory(categoryName));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        Map<String, String> response = new HashMap<>();
        response.put("Message" , "Book deleted successfully");
        return ResponseEntity.ok(response);
    }


}
