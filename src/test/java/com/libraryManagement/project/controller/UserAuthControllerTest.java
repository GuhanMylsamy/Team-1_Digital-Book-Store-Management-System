package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.UserAuthRequestDTO;
import com.libraryManagement.project.dto.requestDTO.UserRequestDTO;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean // FIX: This line was missing, causing the error.
    private UserRepository userRepository;

    @Test
    void registerUser_Success() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("newuser@example.com", "New User", "password123", Role.USER);
        User user = new User();
        when(userService.registerUser(any(UserRequestDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Account created successfully."));
    }

    @Test
    void authenticateAndGetToken_Success() throws Exception {
        UserAuthRequestDTO authRequestDTO = new UserAuthRequestDTO("user@example.com", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User("user@example.com", "password", List.of()),
                null
        );

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        User user = new User();
        user.setUserId(1L);
        user.setEmail("user@example.com");
        user.setRole(Role.USER);

        // This line now works because userRepository is a known mock object.
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("USER"));
    }
}