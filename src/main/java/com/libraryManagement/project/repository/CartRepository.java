package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}