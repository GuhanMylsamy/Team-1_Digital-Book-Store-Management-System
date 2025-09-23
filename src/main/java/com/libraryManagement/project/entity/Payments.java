package com.libraryManagement.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "type",nullable = false)
    private String type;

    @Column(name = "identifier",nullable = false)
    private String identifier;

    @Column(name = "transaction_id",nullable = false)
    private String transactionId;

    @Column(name = "amount",nullable = false)
    private double amount;

    @Column(name = "status",nullable = false)
    private String status;

    @ManyToOne
    @JsonBackReference
    private Order order;

}
