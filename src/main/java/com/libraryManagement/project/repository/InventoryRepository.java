package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByBook(Book book);
}