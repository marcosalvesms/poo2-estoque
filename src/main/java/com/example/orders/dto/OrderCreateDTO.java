package com.example.orders.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record OrderCreateDTO(
        @NotEmpty(message = "Order must have at least one item") List<ItemCreateDTO> items
) {}
