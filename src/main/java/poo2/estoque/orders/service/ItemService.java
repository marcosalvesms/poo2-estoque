package poo2.estoque.orders.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poo2.estoque.orders.dto.ItemCreateDTO;
import poo2.estoque.orders.dto.ItemDTO;
import poo2.estoque.orders.entity.Item;
import poo2.estoque.orders.entity.Order;
import poo2.estoque.orders.repository.ItemRepository;
import poo2.estoque.orders.repository.OrderRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public ItemService(ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    // ========================================================
    // 🔹 Métodos gerais de Item (usados no ItemController)
    // ========================================================

    // Listar todos os itens
    public List<ItemDTO> listAll() {
        return itemRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar item por ID (lança exceção se não encontrar)
    public ItemDTO get(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return toDTO(item);
    }

    // Buscar item por ID (Optional → para uso no controller com map())
    public Optional<ItemDTO> find(Long id) {
        return itemRepository.findById(id).map(this::toDTO);
    }

    // Criar item diretamente
    @Transactional
    public ItemDTO create(ItemDTO dto) {
        Item item = toEntity(dto);
        item = itemRepository.save(item);
        return toDTO(item);
    }

    // Atualizar item
    @Transactional
    public ItemDTO update(Long id, ItemDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());

        item = itemRepository.save(item);
        return toDTO(item);
    }

    // Deletar item
    @Transactional
    public boolean delete(Long id) {
        if (!itemRepository.existsById(id)) {
            return false;
        }
        itemRepository.deleteById(id);
        return true;
    }

    // ========================================================
    // 🔹 Métodos relacionados a Order (usados no OrderItemsController)
    // ========================================================

    // Buscar item dentro de um pedido
    public ItemDTO getByOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Item item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in this order"));

        return toDTO(item);
    }

    // Listar itens de um pedido
    public List<ItemDTO> listByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return order.getItems()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Adicionar item a um pedido
    @Transactional
    public ItemDTO addToOrder(Long orderId, ItemCreateDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Item item = new Item();
        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        item.setOrder(order);

        item = itemRepository.save(item);
        return toDTO(item);
    }

    // Atualizar item dentro de um pedido
    @Transactional
    public ItemDTO updateInOrder(Long orderId, Long itemId, ItemCreateDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Item item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in this order"));

        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());

        item = itemRepository.save(item);
        return toDTO(item);
    }

    // Remover item de um pedido
    @Transactional
    public void deleteFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Item item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in this order"));

        order.getItems().remove(item);
        itemRepository.delete(item);
    }

    // Buscar item específico dentro de um pedido
    @Transactional
    public Optional<ItemDTO> findByOrder(Long orderId, Long itemId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> order.getItems().stream()
                        .filter(i -> i.getId().equals(itemId))
                        .findFirst()
                        .map(this::toDTO));
    }


    // Criar item isolado (não vinculado a um pedido específico)
    @Transactional
    public ItemDTO create(ItemCreateDTO dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        Item saved = itemRepository.save(item);
        return toDTO(saved);
    }

    // Atualizar item isolado
    @Transactional
    public ItemDTO update(Long id, ItemCreateDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        Item updated = itemRepository.save(item);
        return toDTO(updated);
    }


    // ========================================================
    // 🔹 Métodos auxiliares (Entity ↔ DTO)
    // ========================================================

    private ItemDTO toDTO(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }

    private Item toEntity(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        return item;
    }
}
