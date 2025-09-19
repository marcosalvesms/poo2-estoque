package com.example.orders.dto;

import com.example.orders.entity.Item;

public class ItemDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;

    public ItemDTO() {}

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
    }

    public ItemDTO(Long id, String name, Integer quantity, Double price) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.price = price;
}
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
