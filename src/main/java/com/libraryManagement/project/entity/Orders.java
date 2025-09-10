package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Orders {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "status",nullable = false)
    private String status;
    private String paymentId;


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItems> orderItems;

    @OneToOne
    @JoinColumn(name = "order_id")
    private ShippingAddress shippingAddress;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payments> payments;
}
