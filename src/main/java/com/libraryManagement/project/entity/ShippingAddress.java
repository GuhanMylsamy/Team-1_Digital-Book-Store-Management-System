package com.libraryManagement.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shipping_address")
@Data
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_id")
    private Long id;

    @Column(name = "address_line1",length = 100)
    private String addressLine1;

    @Column(name = "address_line2",length = 100)
    private String addressLine2;

    @Column(name = "city",length = 50)
    private String city;

    @Column(name = "country",length = 50)
    private String country;

    @Column(name = "full_name",length = 100)
    private String fullName;

    @Column(name = "phone",length = 255)
    private String phone;

    @Column(name = "postal_code",length = 255)
    private String postalCode;

    @Column(name = "state",length = 50)
    private String state;

    // Relationships

    @OneToOne
    private Order order;
}
