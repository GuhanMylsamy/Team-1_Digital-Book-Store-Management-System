-- This script seeds the database in an order that respects foreign key constraints.

-- 1. Independent Tables: users, authors, categories
-- 👤 USERS
INSERT INTO users (user_id, email, full_name, password, role) VALUES
(1, 'alice@example.com', 'Alice Sharma', 'pass123@', 'USER'),
(2, 'rahul@example.com', 'Rahul Verma', 'pass456@', 'USER'),
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
-- (Added missing authors referenced by books table)
INSERT INTO authors (author_id, name) VALUES (5, 'Martin Fowler');
INSERT INTO authors (author_id, name) VALUES (6, 'Kent Beck');
INSERT INTO authors (author_id, name) VALUES (7, 'Donald Knuth');
INSERT INTO authors (author_id, name) VALUES (8, 'Thomas H. Cormen');
INSERT INTO authors (author_id, name) VALUES (9, 'Andrew S. Tanenbaum');
INSERT INTO authors (author_id, name) VALUES (10, 'Bjarne Stroustrup');
INSERT INTO authors (author_id, name) VALUES (11, 'Kyle Simpson');
INSERT INTO authors (author_id, name) VALUES (12, 'Eric Evans');
INSERT INTO authors (author_id, name) VALUES (13, 'Sam Newman');
INSERT INTO authors (author_id, name) VALUES (14, 'Steve McConnell');
INSERT INTO authors (author_id, name) VALUES (15, 'Andrew Hunt');
INSERT INTO authors (author_id, name) VALUES (16, 'Douglas Crockford');
INSERT INTO authors (author_id, name) VALUES (17, 'Eric Freeman');
INSERT INTO authors (author_id, name) VALUES (18, 'Gayle Laakmann McDowell');
INSERT INTO authors (author_id, name) VALUES (19, 'Aditya Bhargava');
INSERT INTO authors (author_id, name) VALUES (20, 'Brian Goetz');
INSERT INTO authors (author_id, name) VALUES (21, 'Mark Lutz');
INSERT INTO authors (author_id, name) VALUES (22, 'Marijn Haverbeke');
INSERT INTO authors (author_id, name) VALUES (23, 'Martin Kleppmann');


-- 🗂️ CATEGORIES
INSERT INTO categories (category_id, name) VALUES (1, 'Programming');
INSERT INTO categories (category_id, name) VALUES (2, 'Software Design');
INSERT INTO categories (category_id, name) VALUES (3, 'Databases');
-- (Added missing categories referenced by books table)
INSERT INTO categories (category_id, name) VALUES (4, 'Algorithms');
INSERT INTO categories (category_id, name) VALUES (5, 'Operating Systems');
INSERT INTO categories (category_id, name) VALUES (6, 'C++');
INSERT INTO categories (category_id, name) VALUES (7, 'JavaScript');
INSERT INTO categories (category_id, name) VALUES (8, 'Architecture');
INSERT INTO categories (category_id, name) VALUES (9, 'Microservices');
INSERT INTO categories (category_id, name) VALUES (10, 'Software Craftsmanship');
INSERT INTO categories (category_id, name) VALUES (11, 'Python');


-- 2. Tables dependent on users, authors, categories
-- 🏠 SHIPPING ADDRESSES (depends on users)
INSERT INTO shipping_address (shipping_id, address_line1, address_line2, city, country, full_name, phone, postal_code, state, user_id) VALUES (1, '123 MG Road', 'Near Town Hall', 'Coimbatore', 'India', 'Alice Sharma', '9876543210', '641001', 'TN', 1);
INSERT INTO shipping_address (shipping_id, address_line1, address_line2, city, country, full_name, phone, postal_code, state, user_id) VALUES (2, '456 Anna Nagar', 'Opposite Park', 'Chennai', 'India', 'Rahul Verma', '9123456780', '600040', 'TN', 2);

-- 📚 BOOKS (depends on authors, categories)
-- (Added missing books 1-4)
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (1, 'Spring Boot in Action', 1, 1, 499.99, 20, TRUE, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8lPFDdSU9Y7HmNIqHuu0vUg2glpfet86tyQ&s');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (2, 'Effective Java', 2, 1, 599.00, 15, TRUE, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThbrlF5tjpjpYQRBunAMlUu7QEWZkL2MsBsw&s');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (3, 'Clean Code', 3, 1, 450.50, 10, TRUE, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS05_YKBy7T8v3xe1WyrVnm929_No0wDzhXDQ&s');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (4, 'Design Patterns', 3, 2, 399.99, 25, TRUE, 'https://miro.medium.com/1*gp0jKkOUow6G91RQkC90tg.jpeg');
-- (Original books 5-24, which now have valid author/category IDs)
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (5, 'Refactoring: Improving the Design of Existing Code', 5, 2, 650.00, 18, TRUE, 'https://www.informit.com/ShowCover.aspx?isbn=0201485672');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (6, 'The Pragmatic Programmer', 15, 10, 550.75, 30, TRUE, 'https://m.media-amazon.com/images/I/51A8l+FxFNL._SL500_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (7, 'Introduction to Algorithms', 8, 4, 899.00, 12, TRUE, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-yC0aowclqEQRmTCAXw1J6WD6n9fo_NQR1g&s');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (8, 'Code Complete', 14, 10, 720.50, 22, TRUE, 'https://m.media-amazon.com/images/I/61MYY5PRibL._UF1000,1000_QL80_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (9, 'The Art of Computer Programming', 7, 4, 1250.00, 8, TRUE, 'https://m.media-amazon.com/images/I/81Adpoh9IqL._UF1000,1000_QL80_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (10, 'Domain-Driven Design', 12, 8, 680.00, 14, TRUE, 'https://m.media-amazon.com/images/I/818NkFvZy4L._SY522_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (11, 'Building Microservices', 13, 9, 599.99, 25, TRUE, 'https://m.media-amazon.com/images/I/914sPgoyb6L._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (12, 'You Don''t Know JS: Scope & Closures', 11, 7, 350.00, 40, TRUE, 'https://m.media-amazon.com/images/I/71mKvSKoG9L._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (13, 'JavaScript: The Good Parts', 16, 7, 425.00, 35, TRUE, 'https://m.media-amazon.com/images/I/911-3fCci-L._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (14, 'The C++ Programming Language', 10, 6, 850.00, 15, TRUE, 'https://m.media-amazon.com/images/I/61pW2Gbb1DL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (15, 'Head First Design Patterns', 17, 2, 530.00, 28, TRUE, 'https://m.media-amazon.com/images/I/61bDa6hYd-L._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (16, 'Cracking the Coding Interview', 18, 4, 750.00, 50, TRUE, 'https://m.media-amazon.com/images/I/619M-4oN5FL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (17, 'Modern Operating Systems', 9, 5, 950.50, 11, TRUE, 'https://m.media-amazon.com/images/I/81h22026IqL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (18, 'Extreme Programming Explained', 6, 3, 480.00, 17, TRUE, 'https://m.media-amazon.com/images/I/71O15-no1GL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (19, 'Grokking Algorithms', 19, 4, 499.00, 33, TRUE, 'https://m.media-amazon.com/images/I/61uOAw4DJGL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (20, 'Java Concurrency in Practice', 20, 1, 620.00, 19, TRUE, 'https://m.media-amazon.com/images/I/61o2a3aT4XL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (21, 'Clean Architecture', 3, 2, 475.00, 24, TRUE, 'https://m.media-amazon.com/images/I/71s-A1g+JHL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (22, 'Learning Python', 21, 11, 799.00, 20, TRUE, 'https://m.media-amazon.com/images/I/91B5Tln+04L._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (23, 'Eloquent JavaScript', 22, 7, 450.00, 30, TRUE, 'https://m.media-amazon.com/images/I/81-222zC0zL._SY466_.jpg');
INSERT INTO books (book_id, title, author_id, category_id, price, stock_quantity, active, image_url) VALUES (24, 'Designing Data-Intensive Applications', 23, 9, 880.00, 16, TRUE, 'https://m.media-amazon.com/images/I/91rkMMsmkAL._SY466_.jpg');

-- 🛒 CARTS (depends on users)
INSERT INTO cart (cart_id, created_at, updated_at, user_id) VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO cart (cart_id, created_at, updated_at, user_id) VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

-- 3. Tables with multiple dependencies
-- 🧾 INVENTORY (depends on books)
-- (Expanded to include all 24 books)
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (1, 1, 20);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (2, 2, 15);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (3, 3, 10);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (4, 4, 25);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (5, 5, 18);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (6, 6, 30);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (7, 7, 12);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (8, 8, 22);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (9, 9, 8);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (10, 10, 14);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (11, 11, 25);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (12, 12, 40);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (13, 13, 35);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (14, 14, 15);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (15, 15, 28);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (16, 16, 50);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (17, 17, 11);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (18, 18, 17);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (19, 19, 33);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (20, 20, 19);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (21, 21, 24);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (22, 22, 20);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (23, 23, 30);
INSERT INTO inventory (id, book_id, stock_quantity) VALUES (24, 24, 16);

-- 🧺 CART ITEMS (depends on carts, books)
-- (These rows now reference valid book_ids 1, 2, 3)
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id) VALUES (1, 2, 1, 1);
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id) VALUES (2, 1, 3, 1);
INSERT INTO cart_items (cart_item_id, quantity, book_id, cart_id) VALUES (3, 1, 2, 2);

-- 📝 REVIEWS (depends on users, books)
-- (These rows now reference valid book_ids 1, 2, 3)
INSERT INTO reviews (review_id, user_id, book_id, rating, content) VALUES (1, 1, 1, 5.0, 'Excellent book for Spring Boot beginners!');
INSERT INTO reviews (review_id, user_id, book_id, rating, content) VALUES (2, 2, 2, 4.0, 'Very informative and well-written.');
INSERT INTO reviews (review_id, user_id, book_id, rating, content) VALUES (3, 1, 3, 5.0, 'A must-read for clean coding practices.');

-- 4. Order processing tables
-- 📦 ORDERS (depends on users, shipping_address)
INSERT INTO orders (order_id, total_amount, status, payment_id, user_id,  shipping_address) VALUES (1, 1450.48, 'PLACED', 'PAY-ALICE-123', 1, 1);
INSERT INTO orders (order_id, total_amount, status, payment_id, user_id,  shipping_address) VALUES (2, 599.00, 'SHIPPED', 'PAY-RAHUL-456', 2, 2);

-- 📦 ORDER ITEMS (depends on orders, books)
-- (These rows now reference valid book_ids 1, 2, 3)
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id) VALUES (1, 2, 499.99, 1, 1);
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id) VALUES (2, 1, 450.50, 1, 3);
INSERT INTO order_items (item_id, quantity, unit_price, order_id, book_id) VALUES (3, 1, 599.00, 2, 2);

-- 💳 PAYMENTS (depends on orders)
-- Note: The 'order_order_id' column is the default name generated by JPA for the @ManyToOne relationship to the Order entity.
--INSERT INTO payments (payment_id, provider, provider_ref, amount, status, order_order_id) VALUES (1, 'Razorpay', 'RP123XYZ', 1450.48, 'PAID', 1);
--INSERT INTO payments (payment_id, provider, provider_ref, amount, status, order_order_id) VALUES (2, 'Paytm', 'PTM456ABC', 599.00, 'PAID', 2);