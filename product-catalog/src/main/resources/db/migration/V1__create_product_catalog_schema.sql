-- Create category table
CREATE TABLE category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create sub_category table
CREATE TABLE sub_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    category_id INT REFERENCES category (id)
);

-- Create brand table
CREATE TABLE brand
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create product table
CREATE TABLE product
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255)   NOT NULL,
    description     TEXT,
    price           DECIMAL(10, 2) NOT NULL,
    category_id     INT REFERENCES category (id),
    sub_category_id INT REFERENCES sub_category (id),
    brand_id        INT REFERENCES brand (id),
    stock           INT            NOT NULL
);

-- Create specification table
CREATE TABLE specification
(
    id         SERIAL PRIMARY KEY,
    product_id INT REFERENCES product (id) ON DELETE CASCADE,
    key        VARCHAR(255) NOT NULL,
    value      VARCHAR(255) NOT NULL
);

-- Create image table
CREATE TABLE image
(
    id         SERIAL PRIMARY KEY,
    product_id INT REFERENCES product (id) ON DELETE CASCADE,
    url        TEXT NOT NULL
);

-- Create tag table
CREATE TABLE tag
(
    id         SERIAL PRIMARY KEY,
    product_id INT REFERENCES product (id) ON DELETE CASCADE,
    tag        VARCHAR(255) NOT NULL
);

-- Create variant table
CREATE TABLE variant
(
    id         SERIAL PRIMARY KEY,
    product_id INT REFERENCES product (id) ON DELETE CASCADE,
    color      VARCHAR(255),
    stock      INT NOT NULL
);
