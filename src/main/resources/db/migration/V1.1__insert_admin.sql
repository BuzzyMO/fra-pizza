INSERT INTO users(first_name, last_name, email, password, password_salt)
VALUES ('Admin', 'Admin', 'admin@gmail.com',
        '$pbkdf2$pSuQ+Kruo+rGJHnqs+7E6b53O7d6gm3ms5ySy0mgw5k$NQDkffuvm9ivTF5ytXIDWbEfPT/VUUCZUqCp521KIPLoj5H1aB6K+7SNAK/F/GMd4gSDk7l1C4cKlq0p6AAxyw',
        'pSuQ-Kruo-rGJHnqs-7E6b53O7d6gm3ms5ySy0mgw5k');

INSERT INTO user_authorities(user_id, authority_id)
VALUES (1, 1);
