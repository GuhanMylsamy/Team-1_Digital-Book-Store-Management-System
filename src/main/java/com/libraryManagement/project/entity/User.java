package com.libraryManagement.project.entity;
import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    @Column(nullable = false, length = 255)
    private String fullName;
    @Column(length = 255)
    private String newPassword;
    @Column(length = 255)
    private String oldPassword;
    @Column(length = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    //Todo map to Role
    private String role;
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShippingAddress> shippingAddresses;
    // Getters and Setters

    public User(Long userId, String email, String fullName, String newPassword, String oldPassword, String password, String role, List<ShippingAddress> shippingAddresses) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
        this.password = password;
        this.role = role;
        this.shippingAddresses = shippingAddresses;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(List<ShippingAddress> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }
}