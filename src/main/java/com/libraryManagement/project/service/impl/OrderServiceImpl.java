package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.OrderItemRequestDTO;
import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderItemResponseDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import com.libraryManagement.project.entity.*;
import com.libraryManagement.project.repository.*;
import com.libraryManagement.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO){
        User user = userRepository.findById(orderRequestDTO.getUserId()).get();
        Cart cart = cartRepository.findById(orderRequestDTO.getCartId()).get();

        List<CartItems> cartItems = cartItemsRepository.findCartItemsByCartId(cart.getCartId());

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");
        order.setPaymentId("1");

        double totalAmount = 0.0;
        List<OrderItems> orderItems = new ArrayList<>();
        //TODO: use streamapi
        for(CartItems cartItem : cartItems){
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();
            double price = book.getPrice();
            totalAmount += price * quantity;

            OrderItems orderItem = new OrderItems();
            orderItem.setBook(book);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(price);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        ordersRepository.save(order);
        orderItemsRepository.saveAll(orderItems);

        
        return mapToOrderResponseDTO(order,orderItems);

        

    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order, List<OrderItems> orderItems) {

        List<OrderItemResponseDTO> itemDTOs = orderItems.stream().map(item -> {
            OrderItemResponseDTO dto = new OrderItemResponseDTO();
            dto.setOrderItemId(item.getItemId());
            dto.setItemId(item.getBook().getId());
            dto.setItemName(item.getBook().getTitle());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getUnitPrice());
            return dto;
        }).collect(Collectors.toList());

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setPlacedAt(LocalDateTime.now());
        response.setItems(itemDTOs);

        return response;


    }
}
