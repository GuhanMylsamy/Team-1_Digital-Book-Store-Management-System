package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.UserRequestDTO;
import com.libraryManagement.project.dto.requestDTO.UserUpdateDTO;
import com.libraryManagement.project.dto.responseDTO.UserProfileResponseDTO;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.exception.UserAlreadyExistsException;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.security.CustomerUserDetailsService;
import com.libraryManagement.project.service.UserService;
import com.libraryManagement.project.util.SecurityUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(username);
        return userDetail.map(CustomerUserDetailsService::new).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    @Override
    public User registerUser(@Valid UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(STR."User with email \{userRequestDTO.getEmail()} already exists.");
        }
        User user = new User();
        user.setFullName(userRequestDTO.getFullName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findByRole(Role.ADMIN);
    }

    @Override
    public UserProfileResponseDTO getAuthenticatedUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        if (email == null) {
            throw new IllegalStateException("User not authenticated or email not found in principal.");
        }
        User userInfo = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in the database."));

        return UserProfileResponseDTO.builder()
                .userId(userInfo.getUserId())
                .orders(userInfo.getOrders())
                .addresses(userInfo.getAddresses())
                .role(userInfo.getRole())
                .fullName(userInfo.getFullName())
                .email(userInfo.getEmail())
                .build();
    }


    @Transactional
    @Override
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = getUserById(userId);
        existingUser.setFullName(updatedUser.getFullName());
        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found!");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public User updateUserProfile(Long userId, UserUpdateDTO userUpdateDTO) {
        User existingUser = getUserById(userId);
        if (!passwordEncoder.matches(userUpdateDTO.getOldPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Incorrect old password.");
        }
        if (userUpdateDTO.getFullName() != null && !userUpdateDTO.getFullName().isEmpty()) {
            existingUser.setFullName(userUpdateDTO.getFullName());
        }
        existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));

        return userRepository.save(existingUser);
    }
}

