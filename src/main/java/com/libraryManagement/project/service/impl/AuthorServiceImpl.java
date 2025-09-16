package com.libraryManagement.project.service.impl;
import com.libraryManagement.project.entity.Author;
import com.libraryManagement.project.exception.ResourceNotFound;
import com.libraryManagement.project.repository.AuthorRepository;
import com.libraryManagement.project.repository.BookRepository;
import com.libraryManagement.project.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository,BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository=bookRepository;
    }
    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Author not found!"));
    }

    @Override
    public Author getAuthorByName(String name){
        Optional<Author> author = authorRepository.findByName(name);
        if(author.isPresent()) {
            return author.get();
        }else{
            throw new ResourceNotFound("Author not found!");
        }
    }

    @Transactional
    @Override
    public Author addAuthor(Author author) {

        boolean exists = authorRepository.existsByName(author.getName());

        if (exists) {
            throw new IllegalArgumentException("Author already exists in the database.");
        }

        return authorRepository.save(author);
    }

    @Transactional
    @Override
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found!"));
        author.setName(authorDetails.getName());
        return authorRepository.save(author);
    }
    @Transactional
    @Override
    public ResponseEntity<String> deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFound("Author not found!");
        }
        else if (bookRepository.existsByAuthor_AuthorId(id)) {
            throw new IllegalArgumentException("Author name is used by other books, Deletion is not supported.");
        }
        else {
            authorRepository.deleteById(id);
            return ResponseEntity.ok("Author deleted successfully!");
        }
    }
}