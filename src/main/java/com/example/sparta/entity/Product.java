package com.example.sparta.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;

@Entity
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;

    // Product : OrderItem = 1 : N
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();
}