-- 👤 USERS
INSERT INTO users (user_id, email, full_name, password, role)
VALUES (1, 'alice@example.com', 'Alice Sharma', 'pass123', 'USER');

INSERT INTO users (user_id, email, full_name, password, role)
VALUES (2, 'rahul@example.com', 'Rahul Verma', 'pass456', 'USER');

INSERT INTO users (user_id, email, full_name, password, role)
VALUES (3, 'admin@example.com', 'Admin User', 'adminpass', 'ADMIN');

-- 🖋️ AUTHORS
INSERT INTO authors (AuthorID, Name)
VALUES (1, 'Craig Walls');

INSERT INTO authors (AuthorID, Name)
VALUES (2, 'Joshua Bloch');

INSERT INTO authors (AuthorID, Name)
VALUES (3, 'Robert Martin');

-- 🗂️ CATEGORIES
INSERT INTO categories (CategoryID, Name)
VALUES (1, 'Programming');

INSERT INTO categories (CategoryID, Name)
VALUES (2, 'Design');

INSERT INTO categories (CategoryID, Name)
VALUES (3, 'Databases');

-- 📚 BOOKS
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url)
VALUES (1, 'Spring Boot in Action', 1, 1, 499.99, 20, TRUE, 'springboot.jpg');

INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url)
VALUES (2, 'Effective Java', 2, 1, 599.00, 15, TRUE, 'effectivejava.jpg');

INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url)
VALUES (3, 'Clean Code', 3, 1, 450.50, 10, TRUE, 'cleancode.jpg');

INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url)
VALUES (4, 'Design Patterns', 3, 2, 399.99, 25, TRUE, 'designpatterns.jpg');

-- 🧾 INVENTORY
INSERT INTO inventory (id, book_id, stock_quantity)
VALUES (1, 1, 20);

INSERT INTO inventory (id, book_id, stock_quantity)
VALUES (2, 2, 15);

INSERT INTO inventory (id, book_id, stock_quantity)
VALUES (3, 3, 10);

INSERT INTO inventory (id, book_id, stock_quantity)
VALUES (4, 4, 25);

-- 🛒 CART
INSERT INTO cart (cart_id, created_at, updated_at, user_id)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

INSERT INTO cart (cart_id, created_at, updated_at, user_id)
VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

-- 🧺 CART ITEMS
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id)
VALUES (1, 2, 1, 1);

INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id)
VALUES (2, 1, 3, 1);

INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id)
VALUES (3, 1, 2, 2);

-- 📦 ORDERS
INSERT INTO orders (order_id, total_amount, status, payment_id, user_id)
VALUES (1, 1449.48, 'PLACED', 'PAY123', 1);

INSERT INTO orders (order_id, total_amount, status, payment_id, user_id)
VALUES (2, 599.00, 'PLACED', 'PAY456', 2);

-- 📦 ORDER ITEMS
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id)
VALUES (1, 2, 499.99, 1, 1);

INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id)
VALUES (2, 1, 450.50, 1, 3);

INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id)
VALUES (3, 1, 599.00, 2, 2);

-- 💳 PAYMENTS
INSERT INTO payments (payment_id, provider, provider_ref, amount, status, order_order_id)
VALUES (1, 'Razorpay', 'RP123', 1449.48, 'PAID', 1);

INSERT INTO payments (payment_id, provider, provider_ref, amount, status, order_order_id)
VALUES (2, 'Paytm', 'PT456', 599.00, 'PAID', 2);

-- 🏠 SHIPPING ADDRESS
INSERT INTO shipping_address (shipping_id, address_line1, address_line2, city, country, full_name, phone, postal_code, state, order_order_id)
VALUES (1, '123 MG Road', 'Near Town Hall', 'Coimbatore', 'India', 'Alice Sharma', '9876543210', '641001', 'TN', 1);

INSERT INTO shipping_address (shipping_id, address_line1, address_line2, city, country, full_name, phone, postal_code, state, order_order_id)
VALUES (2, '456 Anna Nagar', 'Opposite Park', 'Chennai', 'India', 'Rahul Verma', '9123456780', '600040', 'TN', 2);

-- 📝 REVIEWS
INSERT INTO reviews (review_id, user_id, book_id, rating, content)
VALUES (1, 1, 1, 5.0, 'Excellent book for Spring Boot beginners!');

INSERT INTO reviews (review_id, user_id, book_id, rating, content)
VALUES (2, 2, 2, 4.0, 'Very informative and well-written.');

INSERT INTO reviews (review_id, user_id, book_id, rating, content)
VALUES (3, 1, 3, 5.0, 'A must-read for clean coding practices.');
