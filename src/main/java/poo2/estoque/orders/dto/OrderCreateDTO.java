package poo2.estoque.orders.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderCreateDTO(
        @NotEmpty List<ItemCreateDTO> items
) {
}
