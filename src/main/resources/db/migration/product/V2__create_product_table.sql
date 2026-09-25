CREATE TABLE IF NOT EXISTS product(
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255),
    name        VARCHAR(255),
    price       NUMERIC(12,2),
    status      VARCHAR(255),
    category_id BIGINT,
    constraint fk_product_category
    foreign key (category_id)
    references category (id)
);