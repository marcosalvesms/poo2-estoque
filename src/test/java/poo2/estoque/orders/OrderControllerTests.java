package poo2.estoque.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import poo2.estoque.orders.dto.ItemCreateDTO;
import poo2.estoque.orders.dto.OrderCreateDTO;
import poo2.estoque.orders.repository.OrderRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        orderRepository.deleteAll(); // limpa a tabela antes de cada teste
    }

    @Test
    void createAndListOrder() throws Exception {
        ItemCreateDTO item = new ItemCreateDTO("Teste", 1, 5.0);
        OrderCreateDTO create = new OrderCreateDTO(List.of(item));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk()) // aqui esperamos 200
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }
}
