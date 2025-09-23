package com.libraryManagement.project.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.libraryManagement.project.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = {"orders","addresses"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email",nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "full_name",nullable = false, length = 255)
    private String fullName;

    @Column(name = "new_password",length = 255)
    private String newPassword;

    @Column(name = "old_password",length = 255)
    private String oldPassword;

    @Column(name = "password",length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",length = 50)
    private Role role;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<ShippingAddress> addresses;


}