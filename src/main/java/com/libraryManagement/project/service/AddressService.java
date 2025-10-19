package com.libraryManagement.project.service;

import com.libraryManagement.project.dto.requestDTO.AddressRequestDTO;
import com.libraryManagement.project.entity.ShippingAddress;

import java.util.List;

public interface AddressService {
    ShippingAddress addAddress(Long userId, AddressRequestDTO addressDTO);
    List<ShippingAddress> getUserAddresses(Long userId);
}
