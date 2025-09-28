package com.libraryManagement.project.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String name) {
        super("Book not found " + name);
    }
}
