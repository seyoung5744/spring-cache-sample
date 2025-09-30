package com.example.sparta.dto;

public record ProductReadDto(
        Long id,
        String name,
        int price
) {}