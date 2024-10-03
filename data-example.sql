-- Insert into brand
INSERT INTO brand (name, description)
VALUES ('Logitech', 'A leading manufacturer of computer peripherals.'),
       ('Apple', 'Innovative technology company known for iPhones and Macs.'),
       ('Samsung', 'Global leader in electronics and mobile devices.')
RETURNING id;

-- Insert into category
INSERT INTO category (name, parent_id)
VALUES ('Electronics', NULL),
       ('Accessories', (SELECT id FROM category WHERE name = 'Electronics')),
       ('Computers', (SELECT id FROM category WHERE name = 'Electronics')),
       ('Smartphones', (SELECT id FROM category WHERE name = 'Electronics')),
       ('Peripherals', (SELECT id FROM category WHERE name = 'Computers'))
RETURNING id;

-- Insert into products
INSERT INTO product (name, description, brand_id, price, status, inventory_quantity)
VALUES ('Wireless Mouse', 'A high-quality wireless mouse with ergonomic design.',
        (SELECT id FROM brand WHERE name = 'Logitech'), 29.99, 'active', 150),
       ('MacBook Pro', 'Powerful laptop with M1 chip.',
        (SELECT id FROM brand WHERE name = 'Apple'), 1999.99, 'active', 50),
       ('Galaxy S21', 'Latest Samsung smartphone with advanced features.',
        (SELECT id FROM brand WHERE name = 'Samsung'), 799.99, 'active', 200)
RETURNING id, name;

-- Insert into product_category
-- For 'Wireless Mouse'
INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id
FROM product p,
     category c
WHERE p.name = 'Wireless Mouse'
  AND c.name IN ('Electronics', 'Accessories', 'Peripherals');

-- For 'MacBook Pro'
INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id
FROM product p,
     category c
WHERE p.name = 'MacBook Pro'
  AND c.name IN ('Electronics', 'Computers');

-- For 'Galaxy S21'
INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id
FROM product p,
     category c
WHERE p.name = 'Galaxy S21'
  AND c.name IN ('Electronics', 'Smartphones');

-- Insert into tags
INSERT INTO tag (name)
VALUES ('wireless'),
       ('mouse'),
       ('laptop'),
       ('smartphone'),
       ('bluetooth'),
       ('usb-c')
RETURNING id;

-- Insert into product_tags
-- For 'Wireless Mouse'
INSERT INTO product_tags (product_id, tag_id)
SELECT p.id, t.id
FROM product p,
     tag t
WHERE p.name = 'Wireless Mouse'
  AND t.name IN ('wireless', 'mouse', 'bluetooth');

-- For 'MacBook Pro'
INSERT INTO product_tags (product_id, tag_id)
SELECT p.id, t.id
FROM product p,
     tag t
WHERE p.name = 'MacBook Pro'
  AND t.name IN ('laptop', 'usb-c');

-- For 'Galaxy S21'
INSERT INTO product_tags (product_id, tag_id)
SELECT p.id, t.id
FROM product p,
     tag t
WHERE p.name = 'Galaxy S21'
  AND t.name IN ('smartphone', 'wireless', 'usb-c');

-- Insert into images
-- For 'Wireless Mouse'
INSERT INTO image (product_id, image_url, is_primary)
VALUES ((SELECT id FROM product WHERE name = 'Wireless Mouse'), 'https://cdn.flexishop.com/images/wireless_mouse.jpg',
        TRUE),
       ((SELECT id FROM product WHERE name = 'Wireless Mouse'),
        'https://cdn.flexishop.com/images/wireless_mouse_side.jpg', FALSE);

-- For 'MacBook Pro'
INSERT INTO image (product_id, image_url, is_primary)
VALUES ((SELECT id FROM product WHERE name = 'MacBook Pro'), 'https://cdn.flexishop.com/images/macbook_pro.jpg', TRUE);

-- For 'Galaxy S21'
INSERT INTO image (product_id, image_url, is_primary)
VALUES ((SELECT id FROM product WHERE name = 'Galaxy S21'), 'https://cdn.flexishop.com/images/galaxy_s21.jpg', TRUE);

-- Insert into specifications
-- For 'Wireless Mouse'
INSERT INTO specification (product_id, name, value)
VALUES ((SELECT id FROM product WHERE name = 'Wireless Mouse'), 'Battery Life', '20 hours'),
       ((SELECT id FROM product WHERE name = 'Wireless Mouse'), 'Bluetooth', '5.0'),
       ((SELECT id FROM product WHERE name = 'Wireless Mouse'), 'Color', 'Black');

-- For 'MacBook Pro'
INSERT INTO specification (product_id, name, value)
VALUES ((SELECT id FROM product WHERE name = 'MacBook Pro'), 'Processor', 'M1 Chip'),
       ((SELECT id FROM product WHERE name = 'MacBook Pro'), 'RAM', '16 GB'),
       ((SELECT id FROM product WHERE name = 'MacBook Pro'), 'Storage', '512 GB SSD');

-- For 'Galaxy S21'
INSERT INTO specification (product_id, name, value)
VALUES ((SELECT id FROM product WHERE name = 'Galaxy S21'), 'Screen Size', '6.2 inches'),
       ((SELECT id FROM product WHERE name = 'Galaxy S21'), 'Camera', '64 MP'),
       ((SELECT id FROM product WHERE name = 'Galaxy S21'), 'Battery', '4000 mAh');
