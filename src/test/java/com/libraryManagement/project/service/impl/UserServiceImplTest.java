package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.UserRequestDTO;
import com.libraryManagement.project.dto.requestDTO.UserUpdateDTO;
import com.libraryManagement.project.dto.responseDTO.UserProfileResponseDTO;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.exception.UserAlreadyExistsException;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPassword("hashedPassword");
        user.setRole(Role.USER);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setFullName("Test User");
        userRequestDTO.setPassword("password123");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = userService.registerUser(userRequestDTO);

        assertNotNull(registeredUser);
        assertEquals(userRequestDTO.getEmail(), registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsUserAlreadyExistsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userRequestDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(1L);
        assertNotNull(foundUser);
        assertEquals(user.getUserId(), foundUser.getUserId());
    }

    @Test
    void getUserById_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void loadUserByUsername_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername("test@example.com");
        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_ThrowsUsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    void updateUserProfile_Success() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setOldPassword("hashedPassword");
        updateDTO.setNewPassword("newPassword123");
        updateDTO.setFullName("Updated Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("hashedPassword", "hashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUserProfile(1L, updateDTO);

        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getFullName());
        assertEquals("newHashedPassword", updatedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserProfile_ThrowsBadCredentialsException() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setOldPassword("wrongOldPassword");
        updateDTO.setNewPassword("newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPassword", "hashedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.updateUserProfile(1L, updateDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAuthenticatedUser_Success() {
        // Mocking a static method from SecurityUtil
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

            UserProfileResponseDTO authenticatedUser = userService.getAuthenticatedUser();

            assertNotNull(authenticatedUser);
            assertEquals("test@example.com", authenticatedUser.getEmail());
        }
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ThrowsResourceNotFoundException() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllCustomers_ReturnsAdminUsers() {
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(Collections.singletonList(adminUser));

        List<User> customers = userService.getAllCustomers();

        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
        assertEquals(Role.ADMIN, customers.get(0).getRole());
    }
}