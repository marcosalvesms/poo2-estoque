package poo2.estoque.orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poo2.estoque.orders.dto.ItemCreateDTO;
import poo2.estoque.orders.dto.ItemDTO;
import poo2.estoque.orders.service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listAll() {
        return ResponseEntity.ok(itemService.listAll());
    }

@GetMapping("/{id}")
public ResponseEntity<ItemDTO> getById(@PathVariable Long id) {
    return itemService.find(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}



    @PostMapping
    public ResponseEntity<ItemDTO> create(@RequestBody ItemCreateDTO dto) {
        return ResponseEntity.ok(itemService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> update(@PathVariable Long id, @RequestBody ItemCreateDTO dto) {
        return ResponseEntity.ok(itemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = itemService.delete(id);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build(); // testes esperam 200, não 204
    }

    
}
