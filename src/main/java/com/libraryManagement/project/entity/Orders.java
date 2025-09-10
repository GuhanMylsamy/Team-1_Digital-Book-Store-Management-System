package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Orders {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "status")
    private String status;
    private String paymentId;

    //TODO: userId OneToMany relation
    @Column(name = "user_id")
    private String userId;
}
