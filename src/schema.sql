-- Create the database
CREATE DATABASE collection_assistant;

-- Connect to the database
\c collection_assistant

-- Create the 'users' table
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    is_dealer BOOLEAN NOT NULL
);

-- Create the 'inventory' table
CREATE TABLE inventory (
    item_id BIGSERIAL PRIMARY KEY,
    item_name VARCHAR(30) NOT NULL,
    description TEXT NOT NULL,
    scale VARCHAR(30) NOT NULL,
    added_by BIGINT REFERENCES users(user_id) NOT NULL
);

-- Create the 'user_collections' table
CREATE TABLE user_collections (
    user_id BIGINT REFERENCES users(user_id) NOT NULL,
    item_id BIGINT REFERENCES inventory(item_id) NOT NULL,
    is_favorite BOOLEAN NOT NULL,
    PRIMARY KEY (user_id, item_id)
);

-- Here are my 5 sample queries:

--1) Retrieves all dealers and their emails from the users table:
-- SELECT username, email FROM users WHERE is_dealer = true;

--2) Retrieves the item_name and price columns from the inventory table:
-- SELECT item_name, price FROM inventory;

--3) Sub-query that retrieves the amount of users in the users table and the amount of items in the inventory table:
-- SELECT
-- (SELECT COUNT(*) FROM users) user_count
-- (SELECT COUNT(*) FROM inventory) item_count;

--4) Multi-table join query that retrieves all all items over $100 from the user_collections table and the usernames of the users that they belong to:
-- SELECT i.item_name, u.username
-- FROM users u
-- JOIN user_collections uc ON u.user_id = uc.user_id
-- JOIN inventory i ON uc.item_id = i.item_id
-- WHERE i.price > 100;

-- 5) Multi-table join query that retrieves the items in the users' collections that have Masterpiece collections, and order them by username:
-- SELECT u.username, i.item_name
-- FROM users u
-- JOIN user_collections uc ON u.user_id = uc.user_id
-- JOIN inventory i ON uc.item_id = i.item_id
-- WHERE u.collection_type = 'Masterpiece'
-- ORDER BY u.username;



