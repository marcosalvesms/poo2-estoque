package com.example.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemCreateDTO(
        @NotBlank String name,
        @Min(1) int quantity,
        @Min(0) double price
) {}
