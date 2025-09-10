package com.libraryManagement.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cartItems")
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private String cartItemId;

    //TODO: ManyToOne
    @JoinColumn(name = "carts")
    private String cartId;

    @Column(name = "quantity")
    private int quantity;

    //TODO: ManyToOne
    @JoinColumn(name = "bookId")
    private String bookId;

    public CartItems() {
    }

    public CartItems(String cartItemId, String cartId, int quantity, String bookId) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.quantity = quantity;
        this.bookId = bookId;
    }

    public void cartItems() {}


    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
