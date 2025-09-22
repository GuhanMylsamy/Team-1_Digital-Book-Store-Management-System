package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    @Query("SELECT ci FROM CartItems ci WHERE ci.cart.cartId = :cartId AND ci.book.id = :bookId")
    Optional<CartItems> findByCartCartIdAndBookId(Long cartId, Long bookId);

//    void deleteByCartCartIdAndBookId(Long cartId, Long bookId);
//
//    List<CartItems> findByBookId(Long bookId);

    @Query("SELECT ci FROM CartItems ci WHERE ci.cart.cartId = :cartId")
    List<CartItems> findCartItemsByCartId(Long cartId);

    @Query("SELECT ci FROM CartItems ci WHERE ci.book.bookId = :bookId")
    List<CartItems> findBookByBookId(Long bookId);
}