package com.libraryManagement.project.repository;

import com.libraryManagement.project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    boolean existsByName(String name);
}