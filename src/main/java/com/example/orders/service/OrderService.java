package com.example.orders.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orders.dto.ItemCreateDTO;
import com.example.orders.dto.ItemDTO;
import com.example.orders.dto.OrderCreateDTO;
import com.example.orders.dto.OrderDTO;
import com.example.orders.entity.Item;
import com.example.orders.entity.Order;
import com.example.orders.exception.ResourceNotFoundException;
import com.example.orders.repository.ItemRepository;
import com.example.orders.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<OrderDTO> listAll() {
        return orderRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public OrderDTO get(Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        return toDTO(o);
    }

    @Transactional
    public OrderDTO create(OrderCreateDTO dto) {
        Order o = new Order();
        List<Item> items = dto.items().stream().map(this::toEntity).collect(Collectors.toList());
        items.forEach(i -> i.setOrder(o));
        o.setItems(items);
        Order savedOrder = orderRepository.save(o);
        return toDTO(savedOrder);
    }

    @Transactional
    public OrderDTO update(Long id, OrderCreateDTO dto) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        // replace items
        o.getItems().clear();
        final Order orderRef = o;
        List<Item> items = dto.items().stream().map(this::toEntity).collect(Collectors.toList());
        items.forEach(i -> i.setOrder(orderRef));
        o.setItems(items);
        o = orderRepository.save(o);
        return toDTO(o);
    }

    public void delete(Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        orderRepository.delete(o);
    }

    private OrderDTO toDTO(Order o) {
        List<ItemDTO> items = o.getItems().stream()
                .map(i -> new ItemDTO(i.getId(), i.getName(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());
        return new OrderDTO(o.getId(), o.getCreatedAt(), items, o.getTotal());
    }

    private Item toEntity(ItemCreateDTO dto) {
        Item i = new Item();
        i.setName(dto.getName());
        i.setQuantity(dto.getQuantity());
        i.setPrice(dto.getPrice());
        return i;
    }

}
