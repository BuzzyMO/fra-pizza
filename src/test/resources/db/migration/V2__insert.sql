INSERT INTO users(first_name, last_name, email, password, phone_number)
VALUES ('Vasya', 'Pupkin', 'pupkin@gmail.com', 'testpass', 'testphone123');
INSERT INTO users(first_name, last_name, email, password, phone_number)
VALUES ('Vasya1', 'Pupkin1', 'pupkin1@gmail.com', 'testpass1', 'testphone321');

INSERT INTO pizzerias(city, street, building, latitude, longitude)
VALUES ('Харьков', 'Юрия Гагарина', '20а', 49.98113, 36.24486);
INSERT INTO pizzerias(city, street, building, latitude, longitude)
VALUES ('Харьков', 'Науки проспект', '45/32', 50.03501, 36.21994);
INSERT INTO pizzerias(city, street, building, latitude, longitude)
VALUES ('City', 'Street', '40/30', 50.23501, 36.01994);


INSERT INTO ingredients(name, cost) VALUES ('ingr1', 3);
INSERT INTO ingredients(name, cost) VALUES ('ingr2', 5);
INSERT INTO ingredients(name, cost) VALUES ('ingr3', 1);
INSERT INTO ingredients(name, cost) VALUES ('ingr4', 7);

INSERT INTO pizzas(name, created_by) VALUES ('namePizza', 1);
INSERT INTO pizzas(name, created_by) VALUES ('namePizza1', 2);
INSERT INTO pizzas(name, created_by) VALUES ('namePizza2', 2);

INSERT INTO deliveries(created_by, pizzeria_from, city, street, building)
VALUES(2, 1, 'Харьков', 'Целиноградская', 29);

INSERT INTO orders(delivery_id, pizza_id) VALUES (1, 1);
INSERT INTO orders(delivery_id, pizza_id) VALUES (1, 2);
