package com.example.sparta.service;

import com.example.sparta.dto.OrderItemReadDto;
import com.example.sparta.dto.OrderReadDto;
import com.example.sparta.dto.OrderSimpleDto;
import com.example.sparta.dto.ProductReadDto;
import com.example.sparta.entity.Order;
import com.example.sparta.entity.OrderItem;
import com.example.sparta.entity.Product;
import com.example.sparta.repository.OrderRepository;
import com.example.sparta.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QueryService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public QueryService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Cacheable(cacheNames = "order:detail",
               key = "#orderId",
               unless = "#result == null")
    public OrderReadDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderItemReadDto> items = order.getOrderItems().stream()
                .map(this::toItemDto)
                .toList();

        int totalPrice = items.stream()
                .mapToInt(i -> i.unitPrice() * i.quantity())
                .sum();

        return new OrderReadDto(order.getId(), order.getUser().getId(), items, totalPrice);
    }

    @Cacheable(
            cacheNames = "order:summaries",
            key = "#userId + ':' + #pageable.pageNumber + '-' + #pageable.pageSize",
            condition = "#pageable.pageNumber < 50",
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<OrderSimpleDto> getUserOrderSummaries(Long userId, Pageable pageable) {
        return orderRepository.findSummariesByUserId(userId, pageable);
    }

    @Cacheable(cacheNames = "product:list",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize",
            condition = "#pageable.pageNumber < 50",
            unless = "#result == null || #result.totalElements == 0")
    public Page<ProductReadDto> listProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toProductDto);
    }

    // --- mappers ---
    private OrderItemReadDto toItemDto(OrderItem oi) {
        Product p = oi.getProduct();
        return new OrderItemReadDto(
                p.getId(),
                p.getName(),
                p.getPrice(),
                oi.getQuantity()
        );
    }

    private ProductReadDto toProductDto(Product p) {
        return new ProductReadDto(p.getId(), p.getName(), p.getPrice());
    }
}

