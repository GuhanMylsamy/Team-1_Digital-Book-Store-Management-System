package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Payments")
public class Payments {

    //TODO: paymentId Relation ManyToOne with orders
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String paymentId;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_ref")
    private String providerRef;

    @Column(name = "amount")
    private double amount;

    @Column(name = "status")
    private String status;

}
