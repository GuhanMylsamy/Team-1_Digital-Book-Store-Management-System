package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.BookRequestDTO;
import com.libraryManagement.project.dto.responseDTO.BookResponseDTO;
import com.libraryManagement.project.entity.*;
import com.libraryManagement.project.repository.*;
import com.libraryManagement.project.service.BookService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl  implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final CartItemsRepository cartItemsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CartRepository cartRepository;

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream().filter(book -> book.getActive() == null || book.getActive()).map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO getBookById(Long id){
        Book book = bookRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found"));
        return convertToResponseDTO(book);
    }

    //optional for now, can be implemented when focusing on filter functionality
    public List getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorAuthorId(authorId);
    }

    //TO-DO we can use model mapper in this
    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        // Find existing author by name
        // If the author doesn't exist, create a new Author object, set its name, and save it to the database
        Author author = authorRepository.findByName(bookRequestDTO.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(bookRequestDTO.getAuthorName());
                    return authorRepository.save(newAuthor);
                });

        // Find existing category by name
        // If the category doesn't exist, create a new Category object, set its name, and save it to the database
        Category category = categoryRepository.findByName(bookRequestDTO.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(bookRequestDTO.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        // Check for duplicate book
        // If a book with the same title, author, and category already exists, throw an exception to prevent duplication
        boolean exists = bookRepository.existsByTitleAndAuthorAndCategory(bookRequestDTO.getTitle(), author, category);
        if (exists) {
            throw new IllegalArgumentException("Book already exists in the database.");
        }

        // Convert DTO to Book entity and set its author and category
        Book book = convertToEntity(bookRequestDTO);
        book.setAuthor(author);
        book.setCategory(category);

        // Save the new book to the database
        Book savedBook = bookRepository.save(book);

        // Create inventory record for the new book
        // Set the stock quantity based on the book's initial stock and save it to the inventory
        Inventory inventory = new Inventory();
        inventory.setBook(savedBook);
        inventory.setStockQuantity(savedBook.getStockQuantity());
        inventoryRepository.save(inventory);

        // Convert the saved book entity to a response DTO and return it
        return convertToResponseDTO(savedBook);
    }

    //Need to convert the existing exception to custom BookNotFoundException
    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        // Retrieve the existing book by ID
        // If the book is not found, throw an exception
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));

        // Find or create the author by name
        // If the author doesn't exist, create a new one and save it
        Author author = authorRepository.findByName(bookRequestDTO.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(bookRequestDTO.getAuthorName());
                    return authorRepository.save(newAuthor);
                });

        // Find or create the category by name
        // If the category doesn't exist, create a new one and save it
        Category category = categoryRepository.findByName(bookRequestDTO.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(bookRequestDTO.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        // Update the book's fields with new values from the request DTO
        book.setTitle(bookRequestDTO.getTitle());
        book.setPrice(bookRequestDTO.getPrice());
        book.setStockQuantity(bookRequestDTO.getStockQuantity());
        book.setAuthor(author);
        book.setCategory(category);
        book.setImageUrl(bookRequestDTO.getImageUrl());

        // Save the updated book entity to the database
        Book updatedBook = bookRepository.save(book);

        // Update the inventory stock quantity for the book
        // If an inventory record exists for this book, update its stock quantity
        inventoryRepository.findByBook(updatedBook).ifPresent(inventory -> {
            inventory.setStockQuantity(updatedBook.getStockQuantity());
            inventoryRepository.save(inventory);
        });

        // Convert the updated book entity to a response DTO and return it
        return convertToResponseDTO(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        // Retrieve the book by its ID
        // If the book is not found, throw a custom BookNotFoundException
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));

        // Check if the book is associated with any existing orders
        // If it is, mark the book as inactive instead of deleting it
        boolean isInOrders = orderItemsRepository.existsByBookId(book.getId());
        if (isInOrders) {
            book.setActive(false);
            bookRepository.save(book);
        } else {
            // If the book is not in any orders, proceed with full deletion
            // Remove the book from all shopping carts
            removeBookFromAllCarts(id);

            // Delete the inventory record associated with the book, if it exists
            inventoryRepository.findByBook(book).ifPresent(inventoryRepository::delete);

            // Delete the book from the database
            bookRepository.delete(book);
        }
    }

    private void removeBookFromAllCarts(Long bookId) {
        List<CartItems> cartItems = cartItemsRepository.findByBookId(bookId);
        for (CartItems item : cartItems) {
            Cart cart = item.getCart();
            cart.removeCartItem(item);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            cartItemsRepository.delete(item);
        }
    }


}
