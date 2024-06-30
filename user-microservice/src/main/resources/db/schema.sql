CREATE TABLE IF NOT EXISTS user_login
(
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255)        NOT NULL,
    lastname  VARCHAR(255)        NOT NULL,
    username   VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    enabled    BOOLEAN,
    email      VARCHAR(255) UNIQUE NOT NULL,
    role       VARCHAR(255)
);