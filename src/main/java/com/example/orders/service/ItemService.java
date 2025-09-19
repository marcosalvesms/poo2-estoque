package com.example.orders.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orders.dto.ItemCreateDTO;
import com.example.orders.dto.ItemDTO;
import com.example.orders.entity.Item;
import com.example.orders.exception.ResourceNotFoundException;
import com.example.orders.repository.ItemRepository;


@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(ItemDTO::new)
                .collect(Collectors.toList());
    }

    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return new ItemDTO(item);
    }

    public ItemDTO createItem(ItemCreateDTO dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setQuantity(dto.getQuantity());
        Item saved = itemRepository.save(item);
        return new ItemDTO(saved);
    }

    public ItemDTO updateItem(Long id, ItemCreateDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setQuantity(dto.getQuantity());

        Item updated = itemRepository.save(item);
        return new ItemDTO(updated);
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }
}
