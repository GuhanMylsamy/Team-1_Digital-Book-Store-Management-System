package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.UserUpdateDTO;
import com.libraryManagement.project.dto.responseDTO.UserProfileResponseDTO;
import com.libraryManagement.project.dto.responseDTO.UserResponseDTO;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        UserResponseDTO responseDTO = new UserResponseDTO(
                user.getUserId(),
                user.getFullName(),
                user.getEmail()
        );
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllCustomers() {
        List<User> users = userService.getAllCustomers();
        List<UserResponseDTO> responseDTOs = users.stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getFullName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile() {
        User user = userService.getAuthenticatedUser();
        UserProfileResponseDTO profile = new UserProfileResponseDTO(
                user.getUserId(),
                user.getFullName(),
                user.getEmail()
        );
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<String> updateUserProfile(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        Long currentUserId = userService.getAuthenticatedUser().getUserId();
        userService.updateUserProfile(currentUserId, userUpdateDTO);
        return ResponseEntity.ok("User profile updated successfully.");
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUserAsAdmin(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully by admin.");
    }

    @DeleteMapping("/deleteProfile")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<String> deleteOwnAccount() {
        Long currentUserId = userService.getAuthenticatedUser().getUserId();
        userService.deleteUser(currentUserId);
        return ResponseEntity.ok("Your account has been deleted successfully.");
    }
}

