DROP TABLE IF EXISTS product;

CREATE TABLE IF NOT EXISTS product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT           NOT NULL,
    price       NUMERIC(19, 2) NOT NULL,
    quantity    INTEGER        NOT NULL
);