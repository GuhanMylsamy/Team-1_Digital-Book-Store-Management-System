package com.libraryManagement.project.controller;

import com.libraryManagement.project.dto.requestDTO.AddressRequestDTO;
import com.libraryManagement.project.entity.ShippingAddress;
import com.libraryManagement.project.service.AddressService;
import com.libraryManagement.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<ShippingAddress>> getUserAddresses() {
        Long currentUserId = userService.getAuthenticatedUser().getUserId();
        return ResponseEntity.ok(addressService.getUserAddresses(currentUserId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ShippingAddress> addAddress(@RequestBody @Valid AddressRequestDTO addressDTO) {
        Long currentUserId = userService.getAuthenticatedUser().getUserId();
        ShippingAddress newAddress = addressService.addAddress(currentUserId, addressDTO);
        return ResponseEntity.ok(newAddress);
    }
}