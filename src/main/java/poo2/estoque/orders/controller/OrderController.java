package poo2.estoque.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import poo2.estoque.orders.dto.OrderCreateDTO;
import poo2.estoque.orders.dto.OrderDTO;
import poo2.estoque.orders.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> listAll() {
        return ResponseEntity.ok(orderService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody @Valid OrderCreateDTO dto) {
        // antes retornava 201 CREATED, mas os testes esperam 200 OK
        return ResponseEntity.ok(orderService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id,
                                           @RequestBody @Valid OrderCreateDTO dto) {
        return ResponseEntity.ok(orderService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        // antes retornava 204, mas os testes esperam 200 OK
        return ResponseEntity.ok().build();
    }
}
