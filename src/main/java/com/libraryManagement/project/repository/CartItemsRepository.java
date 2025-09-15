package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    @Query("SELECT b FROM Book b WHERE b.bookId = :bookId")
    List<CartItems> findBookByBookId(@Param("bookId") Long bookId);

//    List<CartItems> findCartByCartId(Long cartId);

    @Query("SELECT ci FROM CartItems ci WHERE ci.cart.cartId = :cartId")
    List<CartItems> findCartItemsByCartId(@Param("cartId") Long cartId);
}