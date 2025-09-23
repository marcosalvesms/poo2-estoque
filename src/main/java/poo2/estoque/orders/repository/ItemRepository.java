package poo2.estoque.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poo2.estoque.orders.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {}
