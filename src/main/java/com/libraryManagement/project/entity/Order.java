package com.libraryManagement.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.libraryManagement.project.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
@ToString(exclude = {"orderItems","payments"})
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private OrderStatus status;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference("user-orders")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonBackReference
    private List<OrderItems> orderItems;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Payments> payments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shipping_address")
    @JsonBackReference
    private ShippingAddress address;
}
