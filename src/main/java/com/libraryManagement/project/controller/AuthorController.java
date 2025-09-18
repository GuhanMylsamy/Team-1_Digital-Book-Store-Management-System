package com.libraryManagement.project.controller;
import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.service.impl.AuthorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorServiceImpl authorService;
    @Autowired
    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @GetMapping("/getAuthor")
    public ResponseEntity<Author> getAuthorByName(@RequestParam("name") String name){
        return ResponseEntity.ok(authorService.getAuthorByName(name));

    }

    @PostMapping("/add")
    public ResponseEntity<Author> addAuthor(@Valid @RequestBody Author author) {
        Author newAuthor = authorService.addAuthor(author);
        return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author authorDetails) {
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return ResponseEntity.ok(updatedAuthor);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        return authorService.deleteAuthor(id);
    }
}