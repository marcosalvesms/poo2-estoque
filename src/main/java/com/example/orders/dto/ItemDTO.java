package com.example.orders.dto;

public record ItemDTO(
        Long id,
        String name,
        int quantity,
        double price
) {}
