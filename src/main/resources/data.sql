-- This script seeds the database in an order that respects foreign key constraints.

-- 1. Independent Tables: users, authors, categories
-- 👤 USERS
INSERT INTO users (user_id, email, full_name, password, role) VALUES
(1, 'alice@example.com', 'Alice Sharma', 'pass123', 'ADMIN'),
(2, 'rahul@example.com', 'Rahul Verma', 'pass456', 'ADMIN'),
(3, 'user@example.com', 'User', 'pass', 'USER'),
(4, 'priya.patel@example.com', 'Priya Patel', 'priya@24', 'USER'),
(5, 'vikram.singh@example.com', 'Vikram Singh', 'vikram#s', 'USER'),
(6, 'anjali.gupta@example.com', 'Anjali Gupta', 'anjali!99', 'USER'),
(7, 'rohan.mehta@example.com', 'Rohan Mehta', 'rohan$m', 'USER'),
(8, 'sneha.reddy@example.com', 'Sneha Reddy', 'sneha@red', 'USER'),
(9, 'arjun.kumar@example.com', 'Arjun Kumar', 'arjun$78', 'USER'),
(10, 'pooja.shah@example.com', 'Pooja Shah', 'pooja!s', 'USER'),
(11, 'amit.joshi@example.com', 'Amit Joshi', 'amit#pass', 'USER'),
(12, 'sunita.iyer@example.com', 'Sunita Iyer', 'sunita@1', 'USER'),
(13, 'karan.malhotra@example.com', 'Karan Malhotra', 'karan!m', 'USER'),
(14, 'meera.desai@example.com', 'Meera Desai', 'meera$45', 'USER'),
(15, 'rajesh.kapoor@example.com', 'Rajesh Kapoor', 'rajesh#k', 'USER'),
(16, 'neha.jain@example.com', 'Neha Jain', 'neha@jain', 'USER'),
(17, 'sanjay.yadav@example.com', 'Sanjay Yadav', 'sanjay!y', 'USER'),
(18, 'geeta.mishra@example.com', 'Geeta Mishra', 'geeta$m', 'USER'),
(19, 'vijay.nagar@example.com', 'Vijay Nagar', 'vijay#987', 'USER'),
(20, 'deepika.pawar@example.com', 'Deepika Pawar', 'deepika@p', 'USER');


-- 🖋️ AUTHORS
INSERT INTO authors (author_id, name) VALUES (1, 'Craig Walls');
INSERT INTO authors (author_id, name) VALUES (2, 'Joshua Bloch');
INSERT INTO authors (author_id, name) VALUES (3, 'Robert Martin');

-- 🗂️ CATEGORIES
INSERT INTO categories (category_id, name) VALUES (1, 'Programming');
INSERT INTO categories (category_id, name) VALUES (2, 'Software Design');
INSERT INTO categories (category_id, name) VALUES (3, 'Databases');

-- 2. Tables dependent on users, authors, categories
-- 🏠 SHIPPING ADDRESSES (depends on users)
INSERT INTO shipping_address (shipping_id, address_line1, address_line2, city, country, full_name, phone, postal_code, state, user_id) VALUES (1, '123 MG Road', 'Near Town Hall', 'Coimbatore', 'India', 'Alice Sharma', '9876543210', '641001', 'TN', 1);
INSERT INTO shipping_address (shipping_id, address_line1, address_line2, city, country, full_name, phone, postal_code, state, user_id) VALUES (2, '456 Anna Nagar', 'Opposite Park', 'Chennai', 'India', 'Rahul Verma', '9123456780', '600040', 'TN', 2);

-- 📚 BOOKS (depends on authors, categories)
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (1, 'Spring Boot in Action', 1, 1, 499.99, 20, TRUE, 'springboot.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (2, 'Effective Java', 2, 1, 599.00, 15, TRUE, 'effectivejava.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (3, 'Clean Code', 3, 1, 450.50, 10, TRUE, 'cleancode.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (4, 'Design Patterns', 3, 2, 399.99, 25, TRUE, 'designpatterns.jpg');

-- 🛒 CARTS (depends on users)
INSERT INTO cart (cart_id, created_at, updated_at, user_id) VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO cart (cart_id, created_at, updated_at, user_id) VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

-- 3. Tables with multiple dependencies
-- 🧾 INVENTORY (depends on books)
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (1, 1, 200);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (2, 2, 150);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (3, 3, 100);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (4, 4, 250);

-- 🧺 CART ITEMS (depends on carts, books)
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id) VALUES (1, 2, 1, 1);
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id) VALUES (2, 1, 3, 1);
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id) VALUES (3, 1, 2, 2);

-- 📝 REVIEWS (depends on users, books)
INSERT INTO reviews (review_id, user_id, book_id, rating, content) VALUES (1, 1, 1, 5.0, 'Excellent book for Spring Boot beginners!');
INSERT INTO reviews (review_id, user_id, book_id, rating, content) VALUES (2, 2, 2, 4.0, 'Very informative and well-written.');
INSERT INTO reviews (review_id, user_id, book_id, rating, content) VALUES (3, 1, 3, 5.0, 'A must-read for clean coding practices.');

-- 4. Order processing tables
-- 📦 ORDERS (depends on users, shipping_address)
INSERT INTO orders (order_id, total_amount, status, payment_id, user_id,  shipping_address) VALUES (1, 1450.48, 'PLACED', 'PAY-ALICE-123', 1, 1);
INSERT INTO orders (order_id, total_amount, status, payment_id, user_id,  shipping_address) VALUES (2, 599.00, 'SHIPPED', 'PAY-RAHUL-456', 2, 2);

-- 📦 ORDER ITEMS (depends on orders, books)
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id) VALUES (1, 2, 499.99, 1, 1);
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id) VALUES (2, 1, 450.50, 1, 3);
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id) VALUES (3, 1, 599.00, 2, 2);

-- 💳 PAYMENTS (depends on orders)
-- Note: The 'order_order_id' column is the default name generated by JPA for the @ManyToOne relationship to the Order entity.
--INSERT INTO payments (payment_id, provider, provider_ref, amount, status, order_order_id) VALUES (1, 'Razorpay', 'RP123XYZ', 1450.48, 'PAID', 1);
--INSERT INTO payments (payment_id, provider, provider_ref, amount, status, order_order_id) VALUES (2, 'Paytm', 'PTM456ABC', 599.00, 'PAID', 2);