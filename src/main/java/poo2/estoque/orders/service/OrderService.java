package poo2.estoque.orders.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import poo2.estoque.orders.dto.ItemCreateDTO;
import poo2.estoque.orders.dto.ItemDTO;
import poo2.estoque.orders.dto.OrderCreateDTO;
import poo2.estoque.orders.dto.OrderDTO;
import poo2.estoque.orders.entity.Item;
import poo2.estoque.orders.entity.Order;
import poo2.estoque.orders.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> listAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO get(Long id) {
        return orderRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public OrderDTO create(OrderCreateDTO dto) {
        Order order = new Order(); // ID null, banco gera
        order.setCreatedAt(LocalDateTime.now());

        List<Item> items = dto.items().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        items.forEach(i -> i.setOrder(order));
        order.setItems(items);

        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    @Transactional
    public OrderDTO update(Long id, OrderCreateDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.getItems().clear();

        List<Item> items = dto.items().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        items.forEach(i -> i.setOrder(order));
        order.getItems().addAll(items);

        Order updated = orderRepository.save(order);
        return toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    // ---------- Conversões ----------

    private Item toEntity(ItemCreateDTO dto) {
        Item i = new Item();
        i.setName(dto.getName());
        i.setQuantity(dto.getQuantity());
        i.setPrice(dto.getPrice());
        return i;
    }

    private ItemDTO toItemDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getQuantity(), item.getPrice());
    }

    private OrderDTO toDTO(Order order) {
        double total = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        List<ItemDTO> items = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        return new OrderDTO(order.getId(), order.getCreatedAt(), items, total);
    }
}
