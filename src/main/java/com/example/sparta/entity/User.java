package com.example.sparta.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // User : Order = 1 : N
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
}