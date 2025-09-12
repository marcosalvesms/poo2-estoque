package com.example.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        LocalDateTime createdAt,
        List<ItemDTO> items,
        double total
) {}
