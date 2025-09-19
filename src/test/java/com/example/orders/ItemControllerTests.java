package com.example.orders;

import com.example.orders.dto.ItemCreateDTO;
import com.example.orders.entity.Item;
import com.example.orders.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        itemRepository.deleteAll();
        itemRepository.save(new Item(null, "Item A", 10.5, 5, null));
        itemRepository.save(new Item(null, "Item B", 20.0, 2, null));
    }

    @Test
    public void testGetAllItems() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetItemById() throws Exception {
        Item item = itemRepository.findAll().get(0);

        mockMvc.perform(get("/items/" + item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    public void testCreateItem() throws Exception {
        ItemCreateDTO dto = new ItemCreateDTO("Item C", 30.0, 10);

        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item C")))
                .andExpect(jsonPath("$.price", is(30.0)))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item item = itemRepository.findAll().get(0);
        ItemCreateDTO dto = new ItemCreateDTO("Item Updated", 50.0, 15);

        mockMvc.perform(put("/items/" + item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item Updated")))
                .andExpect(jsonPath("$.price", is(50.0)))
                .andExpect(jsonPath("$.quantity", is(15)));
    }

    @Test
    public void testDeleteItem() throws Exception {
        Item item = itemRepository.findAll().get(0);

        mockMvc.perform(delete("/items/" + item.getId()))
                .andExpect(status().isNoContent());
    }
}
