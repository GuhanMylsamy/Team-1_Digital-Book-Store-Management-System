package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}