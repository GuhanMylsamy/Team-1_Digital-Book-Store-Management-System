package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findByCartCartIdAndBookId(Long cartId, Long bookId);
    List<CartItems> findByBookId(Long bookId);
}