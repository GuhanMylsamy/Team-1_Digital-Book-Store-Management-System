package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.UserRequestDTO;
import com.libraryManagement.project.dto.requestDTO.UserUpdateDTO;
import com.libraryManagement.project.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {


        @Override
        UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

        @Transactional
        User registerUser(@Valid UserRequestDTO userRequestDTO);

        @Transactional
        User getUserById(Long userId);

        @Transactional
        List<User> getAllCustomers();

        @Transactional
        User getAuthenticatedUser();

        @Transactional
        User updateUser(Long userId, User updatedUser);

        @Transactional
        void deleteUser(Long userId);

        User updateUserProfile(Long userId, UserUpdateDTO userUpdateDTO);
}
