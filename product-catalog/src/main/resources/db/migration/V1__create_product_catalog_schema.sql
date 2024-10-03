-- Enable necessary extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create ENUM type for product status
CREATE TYPE product_status AS ENUM ('active', 'inactive', 'discontinued');

-- Update Timestamp Function using GENERATED ALWAYS AS IDENTITY
CREATE OR REPLACE FUNCTION update_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create table brands
CREATE TABLE brand
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Trigger to update updated_at on row update
CREATE TRIGGER trg_brands_updated_at
    BEFORE UPDATE
    ON brand
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Create table categories
CREATE TABLE category
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    parent_id  BIGINT       REFERENCES category (id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Trigger to update updated_at on row update
CREATE TRIGGER trg_categories_updated_at
    BEFORE UPDATE
    ON category
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Create table products
CREATE TABLE product
(
    id                 UUID PRIMARY KEY        DEFAULT uuid_generate_v4(),
    name               VARCHAR(255)   NOT NULL,
    description        TEXT           NOT NULL,
    brand_id           BIGINT         REFERENCES brand (id) ON DELETE SET NULL,
    price              NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    status             product_status NOT NULL DEFAULT 'active',
    inventory_quantity INTEGER        NOT NULL DEFAULT 0 CHECK (inventory_quantity >= 0),
    created_at         TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Index on name for faster searches
CREATE INDEX idx_products_name ON product (name);

-- Trigger to update updated_at on row update
CREATE TRIGGER trg_products_updated_at
    BEFORE UPDATE
    ON product
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Create table product_categories (junction table)
CREATE TABLE product_categories
(
    product_id  UUID   NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES category (id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, category_id)
);

-- Indexes for faster joins
CREATE INDEX idx_product_categories_product_id ON product_categories (product_id);
CREATE INDEX idx_product_categories_category_id ON product_categories (category_id);

-- Create table tags
CREATE TABLE tag
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Trigger to update updated_at on row update
CREATE TRIGGER trg_tags_updated_at
    BEFORE UPDATE
    ON tag
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Create table product_tags (junction table)
CREATE TABLE product_tags
(
    product_id UUID   NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    tag_id     BIGINT NOT NULL REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, tag_id)
);

-- Indexes for faster joins
CREATE INDEX idx_product_tags_product_id ON product_tags (product_id);
CREATE INDEX idx_product_tags_tag_id ON product_tags (tag_id);

-- Create table images
CREATE TABLE image
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id UUID          NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    image_url  VARCHAR(2048) NOT NULL,
    is_primary BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- Partial index
CREATE UNIQUE INDEX unique_primary_image_per_product
    ON image (product_id)
    WHERE is_primary = TRUE;

-- Index on product_id for faster queries
CREATE INDEX idx_images_product_id ON image (product_id);

-- Trigger to update updated_at on row update
CREATE TRIGGER trg_images_updated_at
    BEFORE UPDATE
    ON image
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Create table specifications
CREATE TABLE specification
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id UUID         NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL,
    value      VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT unique_specification_per_product UNIQUE (product_id, name)
);

-- Index on product_id and name for faster lookups
CREATE INDEX idx_specifications_product_id_name ON specification (product_id, name);

-- Trigger to update updated_at on row update
CREATE TRIGGER trg_specifications_updated_at
    BEFORE UPDATE
    ON specification
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();
