package com.libraryManagement.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.project.dto.requestDTO.UserUpdateDTO;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import com.libraryManagement.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setUserId(1L);
        adminUser.setEmail("admin@example.com");
        adminUser.setFullName("Admin User");
        adminUser.setRole(Role.ADMIN);

        regularUser = new User();
        regularUser.setUserId(2L);
        regularUser.setEmail("user@example.com");
        regularUser.setFullName("Regular User");
        regularUser.setRole(Role.USER);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getUserById_AsUser_Forbidden() throws Exception {
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserById_Unauthenticated_Unauthorized() throws Exception {
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllCustomers_AsAdmin_Success() throws Exception {
        when(userService.getAllCustomers()).thenReturn(Collections.singletonList(adminUser));
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    // --- Authenticated user endpoint tests ---

    @Test
    @WithMockUser(username = "user@example.com", authorities = "USER")
    void getUserProfile_AsUser_Success() throws Exception {
        when(userService.getAuthenticatedUser()).thenReturn(regularUser);
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                // FIX #3: This assertion now also expects the correct email.
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = "USER")
    void updateUserProfile_Success() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setFullName("Updated Name");
        updateDTO.setOldPassword("oldpass");
        updateDTO.setNewPassword("newpass");

        when(userService.getAuthenticatedUser()).thenReturn(regularUser);
        when(userService.updateUserProfile(eq(2L), any(UserUpdateDTO.class))).thenReturn(regularUser);

        mockMvc.perform(put("/users/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User profile updated successfully."));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = "USER")
    void deleteOwnAccount_Success() throws Exception {
        when(userService.getAuthenticatedUser()).thenReturn(regularUser);
        doNothing().when(userService).deleteUser(2L);

        mockMvc.perform(delete("/users/profile")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Your account has been deleted successfully."));
    }
}