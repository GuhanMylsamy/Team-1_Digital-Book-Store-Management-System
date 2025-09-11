package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}