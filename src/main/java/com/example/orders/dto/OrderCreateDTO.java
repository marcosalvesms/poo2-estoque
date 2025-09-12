package com.example.orders.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderCreateDTO(
        @NotEmpty(message = "Order must have at least one item") List<ItemCreateDTO> items
) {}
