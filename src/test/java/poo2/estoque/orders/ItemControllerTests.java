package poo2.estoque.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import poo2.estoque.orders.dto.ItemCreateDTO;
import poo2.estoque.orders.entity.Item;
import poo2.estoque.orders.repository.ItemRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        itemRepository.deleteAll();
        itemRepository.save(new Item("Item A", 5, 10.5, null));
        itemRepository.save(new Item("Item B", 2, 20.0, null));
    }


    @Test
    void testGetAllItems() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Item A"))
                .andExpect(jsonPath("$[0].quantity").value(5))
                .andExpect(jsonPath("$[0].price").value(10.5));
    }

    @Test
    void testGetItemById() throws Exception {
        Item item = itemRepository.findAll().get(0);

        mockMvc.perform(get("/items/" + item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.quantity").value(item.getQuantity()))
                .andExpect(jsonPath("$.price").value(item.getPrice()));
    }

    @Test
    void testCreateItem() throws Exception {
        ItemCreateDTO dto = new ItemCreateDTO("Item C", 3, 30.0);

        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Item C"))
                .andExpect(jsonPath("$.quantity").value(3))
                .andExpect(jsonPath("$.price").value(30.0));
    }

    @Test
    void testUpdateItem() throws Exception {
        Item item = itemRepository.findAll().get(0);
        ItemCreateDTO updateDto = new ItemCreateDTO("Item Updated", 10, 50.0);

        mockMvc.perform(put("/items/" + item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value("Item Updated"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(50.0));
    }

    @Test
    void testDeleteItem() throws Exception {
        Item item = itemRepository.findAll().get(0);

        mockMvc.perform(delete("/items/" + item.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/items/" + item.getId()))
                .andExpect(status().isNotFound());
    }
}
