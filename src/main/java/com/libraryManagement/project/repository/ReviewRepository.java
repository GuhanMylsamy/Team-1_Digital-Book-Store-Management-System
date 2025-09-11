package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

List<Review> findByBookId(Long bookId);
}