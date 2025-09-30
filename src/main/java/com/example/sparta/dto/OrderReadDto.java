package com.example.sparta.dto;

import java.util.List;

public record OrderReadDto(
        Long orderId,
        Long userId,
        List<OrderItemReadDto> items,
        int totalPrice
) {}