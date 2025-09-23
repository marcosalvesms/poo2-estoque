-- Cria uma ordem inicial
INSERT INTO orders (id, created_at) VALUES (1, NOW());

-- Insere os itens conforme os testes esperam
INSERT INTO items (id, name, quantity, price, order_id) VALUES (1, 'Item A', 5, 10.5, 1);
INSERT INTO items (id, name, quantity, price, order_id) VALUES (2, 'Item B', 2, 20.0, 1);
