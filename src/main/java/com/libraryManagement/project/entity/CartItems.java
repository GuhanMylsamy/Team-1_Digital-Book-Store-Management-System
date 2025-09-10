package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "cartItems")
@Data
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;


    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

}
