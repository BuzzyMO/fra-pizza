CREATE TABLE users
(
  id           BIGSERIAL PRIMARY KEY,
  first_name   TEXT NOT NULL,
  last_name    TEXT NOT NULL,
  email        TEXT NOT NULL UNIQUE,
  password     TEXT NOT NULL,
  phone_number TEXT,
  created_at   TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE authorities
(
  id    INT PRIMARY KEY,
  value TEXT NOT NULL
);

CREATE TABLE user_authorities
(
  user_id      BIGINT REFERENCES users (id),
  authority_id INT REFERENCES authorities (id),
  PRIMARY KEY (user_id, authority_id)
);

insert into authorities (id, value)
values (0, 'ROLE_USER');
insert into authorities (id, value)
values (1, 'ROLE_ADMIN');

CREATE TABLE ingredients
(
  id   SERIAL PRIMARY KEY,
  name TEXT  NOT NULL UNIQUE,
  cost MONEY NOT NULL
);

CREATE TABLE pizzas
(
  id         BIGSERIAL PRIMARY KEY,
  name       TEXT   NOT NULL,
  created_by BIGINT NOT NULL REFERENCES users (id),
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE pizza_ingredients
(
  pizza_id      BIGINT REFERENCES pizzas (id),
  ingredient_id INT REFERENCES ingredients (id),
  PRIMARY KEY (pizza_id, ingredient_id)
);

CREATE TABLE pizzerias
(
  id        SERIAL PRIMARY KEY,
  city      TEXT NOT NULL,
  street    TEXT NOT NULL,
  building  TEXT NOT NULL,
  latitude  REAL NOT NULL,
  longitude REAL NOT NULL
);

CREATE TABLE deliveries
(
  id            BIGSERIAL PRIMARY KEY,
  created_by    BIGSERIAL REFERENCES users (id),
  pizzeria_from INT   NOT NULL REFERENCES pizzerias (id),
  city          TEXT  NOT NULL,
  street        TEXT  NOT NULL,
  building      TEXT  NOT NULL,
  apartment     TEXT,
  distance_m    INT   NOT NULL,
  exp_time      TIME  NOT NULL,
  cost          MONEY NOT NULL
);

CREATE TABLE orders
(
  id          SERIAL PRIMARY KEY,
  delivery_id BIGINT REFERENCES deliveries (id),
  pizza_id    BIGINT REFERENCES pizzas (id),
  ordered_at  TIMESTAMPTZ DEFAULT now()
);
