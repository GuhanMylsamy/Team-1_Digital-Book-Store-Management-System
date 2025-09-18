package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Order;
import com.libraryManagement.project.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN TRUE ELSE FALSE END FROM OrderItems oi WHERE oi.book.id = :bookId")
    boolean existsByBookId(@Param("bookId") Long bookId);

    @Query("SELECT o FROM OrderItems o WHERE o.order.user.userId = :userId")
    List<OrderItems> findAllByUserId(Long userId);
}