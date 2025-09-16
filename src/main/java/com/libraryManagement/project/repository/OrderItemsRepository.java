package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN TRUE ELSE FALSE END FROM OrderItems oi WHERE oi.book.id = :bookId")
    boolean existsByBookId(@Param("bookId") Long bookId);
}