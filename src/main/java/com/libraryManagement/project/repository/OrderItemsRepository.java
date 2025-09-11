package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    boolean existsByBookId(Long bookId);
}