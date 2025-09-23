package poo2.estoque.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poo2.estoque.orders.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}
