package com.libraryManagement.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "order_items")
@ToString
public class OrderItems {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price",nullable = false)
    private double unitPrice;

    @ManyToOne
    @JoinColumn(name="order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;
}
