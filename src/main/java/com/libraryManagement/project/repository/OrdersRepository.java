package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {
}