package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.UserAuthRequestDTO;
import com.libraryManagement.project.dto.requestDTO.UserRequestDTO;
import com.libraryManagement.project.dto.responseDTO.UserAuthResponseDTO;
import com.libraryManagement.project.dto.responseDTO.UserProfileResponseDTO;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.enums.Role;
import com.libraryManagement.project.exception.ResourceNotFoundException;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.security.JwtUtil;
import com.libraryManagement.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserAuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secure!";
    }

    /**
     * Handles new user registration.
     * Validation is now handled by @Valid.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        userService.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully.");
    }

    /**
     * Authenticates a user and returns a JWT.
     */

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDTO> authenticateAndGetToken(@RequestBody @Valid UserAuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found after authentication"));
        Role userRole = user.getRole();

        if (userRole == null) {
            throw new IllegalStateException("User '" + username + "' has no role assigned.");
        }
        String token = jwtUtil.generateToken(user.getEmail(), userRole.name(), user.getUserId());
        UserAuthResponseDTO response = new UserAuthResponseDTO(token, userRole);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile() {
        UserProfileResponseDTO authenticatedUser = userService.getAuthenticatedUser();
        return ResponseEntity.ok(authenticatedUser);
    }
}

