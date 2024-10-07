-- 01_create_tables.sql
--liquibase formatted sql
--changeset malyjasiak:1



CREATE TABLE addresses(
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,               -- Unikalne ID użytkownika (autoinkrementacja)
    email VARCHAR(255) UNIQUE NOT NULL,  -- Unikalny adres email
    username VARCHAR(255) NOT NULL,      -- Nazwa użytkownika
    password VARCHAR(255) NOT NULL,      -- Hasło
    roles TEXT,
    address_id INT REFERENCES addresses(id) -- Lista ról użytkownika (przechowywana w formacie JSON)
);

ALTER TABLE addresses
    ADD COLUMN user_id INT,
ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_email ON users (email);



CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id INTEGER REFERENCES users(id)
);

CREATE TABLE units(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(255),
    conversion_to_grams DOUBLE PRECISION,
    user_id INTEGER REFERENCES users(id),
    CONSTRAINT unique_name_user UNIQUE (name, user_id)
);


CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    category_id INTEGER NOT NULL REFERENCES categories(id),
    price DECIMAL(10,2) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id)
);

CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity FLOAT NOT NULL,
    unit_id INTEGER REFERENCES units(id),
    expiry_date TIMESTAMP,
    user_id INTEGER NOT NULL REFERENCES users(id)
);

CREATE TABLE menu_item_ingredients(
    menu_item_id INTEGER NOT NULL REFERENCES menu_items(id),
    ingredient_id INTEGER NOT NULL REFERENCES ingredients(id),
    quantity_required FLOAT NOT NULL
);

CREATE TABLE orders(
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    order_date TIMESTAMP NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
);

CREATE TABLE order_items(
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id),
    menu_item_id INTEGER NOT NULL REFERENCES menu_items(id),
    quantity INTEGER NOT NULL,
    price_at_order DECIMAL(10,2) NOT NULL
);

CREATE TYPE status_type AS ENUM ('Pending', 'Received');

CREATE TABLE supply_orders(
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    order_date TIMESTAMP NOT NULL,
    delivery_date TIMESTAMP NOT NULL,
    status status_type NOT NULL
);

CREATE TABLE supply_order_items(
    id SERIAL PRIMARY KEY,
    supply_order_id INTEGER NOT NULL REFERENCES supply_orders(id),
    ingredient_id INTEGER NOT NULL REFERENCES ingredients(id),
    quantity FLOAT NOT NULL,
    price_at_order DECIMAL(10,2) NOT NULL
);

CREATE TABLE sales_report(
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) NOT NULL,
    report_date DATE NOT NULL,
    total_sales DECIMAL(10,2) NOT NULL,
    most_sold_item INTEGER NOT NULL REFERENCES menu_items(id)
);

CREATE TABLE predictions(
    id SERIAL PRIMARY KEY,
    ingredient_id INTEGER NOT NULL REFERENCES ingredients(id),
    user_id INTEGER NOT NULL REFERENCES users(id),
    predicted_usage FLOAT NOT NULL,
    prediction_date DATE NOT NULL
);

CREATE TYPE change_type AS ENUM ('Used', 'Received', 'Adjusted', 'Losses');

CREATE TABLE inventoryLog(
    id SERIAL PRIMARY KEY,
    ingredient_id INTEGER NOT NULL REFERENCES ingredients(id),
    user_id INTEGER NOT NULL REFERENCES users(id),
    change_date TIMESTAMP NOT NULL,
    change_type change_type NOT NULL,
    quantity FLOAT NOT NULL
);

