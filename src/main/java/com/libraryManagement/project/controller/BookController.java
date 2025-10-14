package com.libraryManagement.project.controller;


import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.service.impl.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true")
public class BookController {
    private final BookServiceImpl bookService;

    @Autowired
    public BookController(BookServiceImpl bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponseDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookResponseDTO>> getBooksByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addBook")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO bookRequestDTO){
        BookResponseDTO createdBook = bookService.addBook(bookRequestDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
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

//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        Map<String, String> response = new HashMap<>();
        response.put("Message" , "Book deleted successfully");
        return ResponseEntity.ok(response);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/addBookWithImage", consumes = "multipart/form-data")
    public ResponseEntity<BookResponseDTO> addBookWithImage(
            @RequestPart("book") BookRequestDTO bookRequestDTO,
            @RequestPart("image") MultipartFile imageFile
    ) {
        BookResponseDTO responseDTO = bookService.addBookWithImage(bookRequestDTO, imageFile);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/updateBookWithImage/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BookResponseDTO> updateBookWithImage(
            @PathVariable Long id,
            @RequestPart("book") BookRequestDTO bookRequestDTO,
            @RequestPart("image") MultipartFile imageFile
    ) {
        BookResponseDTO responseDTO = bookService.updateBookWithImage(id, bookRequestDTO, imageFile);
        return ResponseEntity.ok(responseDTO);
    }

}
