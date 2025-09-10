package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.IdentityHashMap;

@Entity
@Data
@Table(name = "order_items")
public class OrderItems {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String itemId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    //TODO: orderId Relation ManyToOne
    @Column(name = "order_id")
    private String orderId;

    //TODO: bookId Relation ManyToOne
    @Column(name = "book_id")
    private String bookId;
}
