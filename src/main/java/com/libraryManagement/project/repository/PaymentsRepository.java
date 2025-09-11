package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
}