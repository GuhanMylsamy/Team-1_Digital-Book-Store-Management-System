package com.libraryManagement.project.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.libraryManagement.project.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
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
    @JsonManagedReference("user-orders")
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-addresses")
    private List<ShippingAddress> addresses;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(STR."ROLE_\{this.role.name()}"));
    }
}