package com.libraryManagement.project.security;

import com.libraryManagement.project.enums.Role;
import com.libraryManagement.project.util.CustomUserPrinciple;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtUtil jwtUtil;

    // FIX: Use constructor injection for dependencies in filters.
    // @Autowired on fields will NOT work here because filters are part of the servlet container's chain
    // and are initialized before Spring's full dependency injection context is ready. This prevents NullPointerExceptions.
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            Role role = jwtUtil.extractRole(token);
            Long userId = jwtUtil.extractUserId(token);


            var authorities = List.of(new SimpleGrantedAuthority(role.name()));

            // This custom principal will be available throughout the application for authenticated users.
            CustomUserPrinciple customUserPrincipal = new CustomUserPrinciple(userId, email);

            var authentication = new UsernamePasswordAuthenticationToken(
                    customUserPrincipal, // Set our custom principal object
                    null,
                    authorities
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}