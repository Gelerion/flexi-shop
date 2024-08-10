INSERT INTO category (name)
VALUES ('Electronics'),
       ('Home Appliances'),
       ('Books'),
       ('Clothing');

INSERT INTO sub_category (name, category_id)
VALUES ('Smartphones', 1),
       ('Laptops', 1),
       ('Refrigerators', 2),
       ('Fiction', 3),
       ('Non-Fiction', 3),
       ('Mens Clothing', 4),
       ('Womens Clothing', 4);

INSERT INTO brand (name)
VALUES ('Apple'),
       ('Samsung'),
       ('LG'),
       ('Sony'),
       ('Penguin'),
       ('H&M');

INSERT INTO product (name, description, price, category_id, sub_category_id, brand_id, stock)
VALUES ('iPhone 14', 'Latest Apple smartphone with A16 chip.', 999.99, 1, 1, 1, 50),
       ('Galaxy S22', 'Flagship Samsung smartphone with powerful features.', 899.99, 1, 1, 2, 40),
       ('MacBook Air', 'Lightweight and powerful laptop with M1 chip.', 1199.99, 1, 2, 1, 30),
       ('LG Refrigerator', 'Energy-efficient refrigerator with smart cooling.', 799.99, 2, 3, 3, 20),
       ('The Great Gatsby', 'Classic novel by F. Scott Fitzgerald.', 10.99, 3, 4, 5, 100),
       ('H&M Mens T-Shirt', 'Comfortable cotton t-shirt.', 19.99, 4, 6, 6, 200);

INSERT INTO specification (product_id, key, value)
VALUES (1, 'Display', '6.1-inch OLED'),
       (1, 'Processor', 'A16 Bionic'),
       (2, 'Display', '6.2-inch AMOLED'),
       (2, 'Processor', 'Exynos 2100'),
       (3, 'RAM', '8GB'),
       (3, 'Storage', '256GB SSD'),
       (4, 'Capacity', '500L'),
       (4, 'Energy Rating', 'A++'),
       (5, 'Author', 'F. Scott Fitzgerald'),
       (5, 'Genre', 'Fiction'),
       (6, 'Material', '100% Cotton'),
       (6, 'Size', 'L');


INSERT INTO image (product_id, url)
VALUES (1, 'https://example.com/images/iphone14.jpg'),
       (2, 'https://example.com/images/galaxys22.jpg'),
       (3, 'https://example.com/images/macbookair.jpg'),
       (4, 'https://example.com/images/lgfridge.jpg'),
       (5, 'https://example.com/images/greatgatsby.jpg'),
       (6, 'https://example.com/images/hmtshirt.jpg');
