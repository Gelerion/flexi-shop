-- Create category table
CREATE TABLE category (
                          category_id SERIAL PRIMARY KEY,
                          name VARCHAR(255) UNIQUE NOT NULL
);

-- Create sub_category table
CREATE TABLE sub_category (
                              sub_category_id SERIAL PRIMARY KEY,
                              name VARCHAR(255) UNIQUE NOT NULL,
                              category_id INT REFERENCES category(category_id)
);

-- Create brand table
CREATE TABLE brand (
                       brand_id SERIAL PRIMARY KEY,
                       name VARCHAR(255) UNIQUE NOT NULL
);

-- Create product table
CREATE TABLE product (
                         product_id VARCHAR(255) PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10, 2) NOT NULL,
                         category_id INT REFERENCES category(category_id),
                         sub_category_id INT REFERENCES sub_category(sub_category_id),
                         brand_id INT REFERENCES brand(brand_id),
                         stock INT NOT NULL
);

-- Create specification table
CREATE TABLE specification (
                               id SERIAL PRIMARY KEY,
                               product_id VARCHAR(255) REFERENCES product(product_id) ON DELETE CASCADE,
                               key VARCHAR(255) NOT NULL,
                               value VARCHAR(255) NOT NULL
);

-- Create image table
CREATE TABLE image (
                       id SERIAL PRIMARY KEY,
                       product_id VARCHAR(255) REFERENCES product(product_id) ON DELETE CASCADE,
                       url TEXT NOT NULL
);

-- Create tag table
CREATE TABLE tag (
                     id SERIAL PRIMARY KEY,
                     product_id VARCHAR(255) REFERENCES product(product_id) ON DELETE CASCADE,
                     tag VARCHAR(255) NOT NULL
);

-- Create variant table
CREATE TABLE variant (
                         variant_id VARCHAR(255) PRIMARY KEY,
                         product_id VARCHAR(255) REFERENCES product(product_id) ON DELETE CASCADE,
                         color VARCHAR(255),
                         stock INT NOT NULL
);
