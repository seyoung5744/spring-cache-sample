package com.example.sparta.dto;

public record OrderItemReadDto(
        Long productId,
        String productName,
        int unitPrice,
        int quantity
) {}
