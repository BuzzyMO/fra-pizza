INSERT INTO users(first_name, last_name, email, password)
VALUES ('Admin', 'Admin', 'admin@gmail.com', 'changeme');

INSERT INTO user_authorities(user_id, authority_id) VALUES (1, 1);
