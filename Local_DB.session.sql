CREATE TABLE product (
    product_id serial,
    name VARCHAR(200) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    added_at DATE NOT NULL,
	labels TEXT[],
	PRIMARY KEY (product_id)
);

CREATE TABLE products (
    id serial,
    cart_id BIGINT NOT NULL REFERENCES shopping_carts(id),
    product_id BIGINT NOT NULL REFERENCES product(product_id),
    quantity INT NOT NULL,
    CHECK (quantity > 0),
	PRIMARY KEY(id)
);

CREATE TABLE shopping_carts (
    id serial,
	checked_out BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);