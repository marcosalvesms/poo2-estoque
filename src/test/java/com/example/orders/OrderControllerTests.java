package com.example.orders;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
<<<<<<< HEAD

import com.example.orders.dto.ItemCreateDTO;
import com.example.orders.dto.OrderCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
=======
>>>>>>> e377ecdd8c008d558a640350f8da95df0f6c2751

import com.example.orders.controller.OrderController;
import com.example.orders.dto.ItemCreateDTO;
import com.example.orders.dto.OrderCreateDTO;
import com.example.orders.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
class OrderControllerTests {

        @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService; // mock de todos os services usados
   
    @Autowired private ObjectMapper objectMapper;

    @Test
    void createAndListOrder() throws Exception {
        ItemCreateDTO item = new ItemCreateDTO("Teste", 5.0, 1);
        OrderCreateDTO create = new OrderCreateDTO(List.of(item));
        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(create)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/orders"))
            .andExpect(status().isOk());
    }
}
