CREATE TABLE users
(
  id           BIGSERIAL PRIMARY KEY,
  first_name   TEXT NOT NULL,
  last_name    TEXT NOT NULL,
  email        TEXT NOT NULL,
  password     TEXT NOT NULL,
  phone_number TEXT,
  created_at   TIMESTAMPTZ DEFAULT now()
);

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

CREATE TABLE deliveries
(
  id        BIGSERIAL PRIMARY KEY,
  user_id   BIGSERIAL REFERENCES users (id),
  city      TEXT  NOT NULL,
  street    TEXT  NOT NULL,
  building  TEXT  NOT NULL,
  apartment TEXT,
  exp_time  TIME  NOT NULL,
  cost      MONEY NOT NULL
);

CREATE TABLE orders
(
  id          SERIAL PRIMARY KEY,
  delivery_id BIGINT REFERENCES deliveries (id),
  pizza_id    BIGINT REFERENCES pizzas (id),
  ordered_at  TIMESTAMPTZ DEFAULT now()
);
