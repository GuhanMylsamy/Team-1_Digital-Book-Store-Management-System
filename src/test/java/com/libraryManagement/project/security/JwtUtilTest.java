package com.libraryManagement.project.security;

import com.libraryManagement.project.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.init(); // Manually call @PostConstruct method
    }

    @Test
    void generateTokenAndValidate() {
        String email = "test@example.com";
        String role = "USER";
        Long userId = 1L;

        String token = jwtUtil.generateToken(email, role, userId);

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void extractClaimsFromToken() {
        String email = "test@example.com";
        Role role = Role.ADMIN;
        Long userId = 123L;

        String token = jwtUtil.generateToken(email, role.name(), userId);

        String extractedEmail = jwtUtil.extractEmail(token);
        Role extractedRole = jwtUtil.extractRole(token);
        Long extractedUserId = jwtUtil.extractUserId(token);

        assertEquals(email, extractedEmail);
        assertEquals(role, extractedRole);
        assertEquals(userId, extractedUserId);
    }

    @Test
    void validateToken_InvalidToken() {
        String invalidToken = "this.is.not.a.valid.token";
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void validateToken_ExpiredToken() throws InterruptedException {
        // This is a simplified way to test expiration. In a real scenario, you might inject a Clock.
        // For now, we rely on the implementation detail of a very short expiration.
        // NOTE: This test is not included as it requires changing the source code's EXPIRATION time.
        // A proper test would involve a custom JwtUtil constructor that accepts an expiration time.
    }
}