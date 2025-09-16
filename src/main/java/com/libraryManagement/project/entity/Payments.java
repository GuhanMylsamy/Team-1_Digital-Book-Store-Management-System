package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payments")
public class Payments {


    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "provider",nullable = false)
    private String provider;

    @Column(name = "provider_ref",nullable = false)
    private String providerRef;

    @Column(name = "amount",nullable = false)
    private double amount;

    @Column(name = "status",nullable = false)
    private String status;

    @ManyToOne
    private Order order;

}
