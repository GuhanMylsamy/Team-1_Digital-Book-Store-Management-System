package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.query.Order;

import java.util.IdentityHashMap;

@Entity
@Data
@Table(name = "order_items")
public class OrderItems {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price",nullable = false)
    private double unitPrice;

    //TODO: orderId Relation ManyToOne
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    //TODO: bookId Relation ManyToOne
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookId;
}
