package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Book;
import com.libraryManagement.project.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByBook(Book book);
    List<Inventory> findByStockQuantityLessThan(int threshold);
}