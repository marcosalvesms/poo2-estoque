package poo2.estoque.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import poo2.estoque.orders.dto.ItemCreateDTO;
import poo2.estoque.orders.dto.ItemDTO;
import poo2.estoque.orders.service.ItemService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders/{orderId}/items")
public class OrderItemsController {

    private final ItemService itemService;

    public OrderItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(itemService.listByOrder(orderId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDTO> getByIdInOrder(@PathVariable Long orderId,
                                                  @PathVariable Long itemId) {
        Optional<ItemDTO> item = itemService.findByOrder(orderId, itemId);
        return item.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemDTO> addToOrder(@PathVariable Long orderId,
                                              @RequestBody @Valid ItemCreateDTO dto) {
        return ResponseEntity.ok(itemService.addToOrder(orderId, dto));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateInOrder(@PathVariable Long orderId,
                                                @PathVariable Long itemId,
                                                @RequestBody @Valid ItemCreateDTO dto) {
        ItemDTO updated = itemService.updateInOrder(orderId, itemId, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteFromOrder(@PathVariable Long orderId,
                                                @PathVariable Long itemId) {
        itemService.deleteFromOrder(orderId, itemId);
        return ResponseEntity.ok().build(); // testes esperam 200 OK
    }


}
