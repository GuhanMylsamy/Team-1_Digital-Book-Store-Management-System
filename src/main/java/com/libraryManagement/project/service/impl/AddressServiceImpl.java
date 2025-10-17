package com.libraryManagement.project.service.impl;


import com.libraryManagement.project.dto.requestDTO.AddressRequestDTO;
import com.libraryManagement.project.entity.ShippingAddress;
import com.libraryManagement.project.entity.User;
import com.libraryManagement.project.repository.ShippingAddressRepository;
import com.libraryManagement.project.repository.UserRepository;
import com.libraryManagement.project.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final ShippingAddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(ShippingAddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShippingAddress addAddress(Long userId, AddressRequestDTO addressDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ShippingAddress newAddress = new ShippingAddress();
        newAddress.setFullName(addressDTO.getFullName());
        newAddress.setPhone(addressDTO.getPhone());
        newAddress.setAddressLine1(addressDTO.getAddressLine1());
        newAddress.setAddressLine2(addressDTO.getAddressLine2());
        newAddress.setCity(addressDTO.getCity());
        newAddress.setState(addressDTO.getState());
        newAddress.setPostalCode(addressDTO.getPostalCode());
        newAddress.setCountry(addressDTO.getCountry());
        newAddress.setUser(user);

        return addressRepository.save(newAddress);
    }

    @Override
    public List<ShippingAddress> getUserAddresses(Long userId) {
        return addressRepository.findByUserUserId(userId); // Assuming you create this method in the repo
    }

}

