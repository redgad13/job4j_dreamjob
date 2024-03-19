CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    varchar UNIQUE not null,
    password varchar not null
);

INSERT INTO users (email, password) VALUES ('user@ya.ru', 'aaa');