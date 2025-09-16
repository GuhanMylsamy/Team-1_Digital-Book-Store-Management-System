package com.libraryManagement.project.service.impl;

import com.libraryManagement.project.dto.requestDTO.BuyNowRequestDTO;
import com.libraryManagement.project.dto.requestDTO.OrderItemRequestDTO;
import com.libraryManagement.project.dto.requestDTO.OrderRequestDTO;
import com.libraryManagement.project.dto.responseDTO.OrderItemResponseDTO;
import com.libraryManagement.project.dto.responseDTO.OrderResponseDTO;
import com.libraryManagement.project.entity.*;
import com.libraryManagement.project.enums.OrderStatus;
import com.libraryManagement.project.exception.ResourceNotFound;
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

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final BookRepository bookRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, CartRepository cartRepository, CartItemsRepository cartItemsRepository, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, ShippingAddressRepository shippingAddressRepository, BookRepository bookRepository, InventoryRepository inventoryRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO){

        //Fetch User, Cart, ShippingAddress Object from database using their IDs

        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFound("User does not exist"));

        Cart cart = cartRepository.findById(orderRequestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFound("Cart does not exist"));


        ShippingAddress address = shippingAddressRepository.findById(orderRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFound("Address does not exist"));

        List<CartItems> cartItems = cartItemsRepository.findCartItemsByCartId(cart.getCartId());

        if(cartItems.isEmpty()){
            throw new ResourceNotFound("Cart is empty");
        }

        //Creating Order object
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setPaymentId("1");
        order.setAddress(address);


        double totalAmount = 0.0;
        List<OrderItems> orderItems = new ArrayList<>();

        //Calculating total cost for all items
        totalAmount = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getBook().getPrice() * cartItem.getQuantity())
                .sum();

        //Mapping cartItems to orderItems
        orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItems orderItem = new OrderItems();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setUnitPrice(cartItem.getBook().getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        //Updating inventory
        orderItems.forEach(orderItem -> {
            Book book = orderItem.getBook();
            int orderQuantity = orderItem.getQuantity();

            Inventory inventory = inventoryRepository.findByBook(book)
                    .orElseThrow(() -> new ResourceNotFound("No inventory present"));

            int stockQuantity = inventory.getStockQuantity();
            int updatedQuantity = stockQuantity - orderQuantity;

            inventory.setStockQuantity(updatedQuantity);

            inventoryRepository.save(inventory);
        });

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        ordersRepository.save(order);
        orderItemsRepository.saveAll(orderItems);

        
        return mapToOrderResponseDTO(order,orderItems);

        

    }

    @Override
    public OrderResponseDTO buyNow(BuyNowRequestDTO buyNowRequestDTO){

        //Fetch User, Cart, ShippingAddress Object from database using their IDs

        User user = userRepository.findById(buyNowRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFound("User does not exist"));

        ShippingAddress address = shippingAddressRepository.findById(buyNowRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFound("Address does not exist"));

        Book book = bookRepository.findById(buyNowRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFound("Book does not exist"));

        int quantity = buyNowRequestDTO.getQuantity();
        double price = book.getPrice();

        //Creating orderItem object
        OrderItems orderItem = new OrderItems();
        orderItem.setBook(book);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(price);

        //updating inventory
        Inventory inventory = inventoryRepository.findByBook(book)
                .orElseThrow(() -> new ResourceNotFound("No inventory present"));

        int stockQuantity = inventory.getStockQuantity();
        int updatedQuantity = stockQuantity - quantity;

        inventoryRepository.save(inventory);

        //Creating Order Object
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setPaymentId("1");
        order.setAddress(address);
        order.setTotalAmount(price);

        List<OrderItems> orderItems = new ArrayList<>();

        orderItem.setOrder(order);
        orderItems.add(orderItem);

        order.setOrderItems(orderItems);

        ordersRepository.save(order);
        orderItemsRepository.saveAll(orderItems);

        return mapToOrderResponseDTO(order,orderItems);



    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order, List<OrderItems> orderItems) {

        //Mapping orderItems to OrderItemResponseDTO
        List<OrderItemResponseDTO> itemDTOs = orderItems.stream().map(item -> {
            OrderItemResponseDTO dto = new OrderItemResponseDTO();
            dto.setOrderItemId(item.getItemId());
            dto.setItemId(item.getBook().getBookId());
            dto.setItemName(item.getBook().getTitle());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getUnitPrice());
            return dto;
        }).collect(Collectors.toList());

        //Mapping Order to OrderResponseDTO
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setAddressId(order.getAddress().getAddressId());
        response.setTotalAmount(order.getTotalAmount());
        response.setPlacedAt(LocalDateTime.now());
        response.setItems(itemDTOs);

        return response;


    }
}
