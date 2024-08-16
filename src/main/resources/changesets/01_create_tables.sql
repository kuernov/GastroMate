-- 01_create_tables.sql
--liquibase formatted sql
--changeset malyjasiak:1

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE addresses(
    id SERIAL PRIMARY KEY,
    userId INTEGER NOT NULL REFERENCES users(id),
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postalCode VARCHAR(255) NOT NULL
);

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE menuItems (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    categoryId INTEGER NOT NULL REFERENCES categories(id),
    price DECIMAL(10,2) NOT NULL,
    userId INTEGER NOT NULL REFERENCES users(id)
);

CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity FLOAT NOT NULL,
    unit VARCHAR(50) NOT NULL,
    expiryDate TIMESTAMP,
    userId INTEGER NOT NULL REFERENCES users(id)
);

CREATE TABLE menuItemIngredients(
    menuItemId INTEGER NOT NULL REFERENCES menuItems(id),
    ingredientId INTEGER NOT NULL REFERENCES ingredients(id),
    quantityRequired FLOAT NOT NULL
);

CREATE TABLE orders(
    id SERIAL PRIMARY KEY,
    userId INTEGER NOT NULL REFERENCES users(id),
    orderDate TIMESTAMP NOT NULL,
    totalPrice DECIMAL(10,2) NOT NULL
);

CREATE TABLE orderItems(
    id SERIAL PRIMARY KEY,
    orderId INTEGER NOT NULL REFERENCES orders(id),
    menuItemId INTEGER NOT NULL REFERENCES menuItems(id),
    quantity INTEGER NOT NULL,
    priceAtOrder DECIMAL(10,2) NOT NULL
);

CREATE TYPE status_type AS ENUM ('Pending', 'Received');

CREATE TABLE supplyOrders(
    id SERIAL PRIMARY KEY,
    userId INTEGER NOT NULL REFERENCES users(id),
    orderDate TIMESTAMP NOT NULL,
    deliveryDate TIMESTAMP NOT NULL,
    status status_type NOT NULL
);

CREATE TABLE supplyOrderItems(
    id SERIAL PRIMARY KEY,
    supplyOrderId INTEGER NOT NULL REFERENCES supplyOrders(id),
    ingredientId INTEGER NOT NULL REFERENCES ingredients(id),
    quantity FLOAT NOT NULL,
    priceAtOrder DECIMAL(10,2) NOT NULL
);

CREATE TABLE salesReport(
    id SERIAL PRIMARY KEY,
    userId INTEGER REFERENCES users(id) NOT NULL,
    reportDate DATE NOT NULL,
    totalSales DECIMAL(10,2) NOT NULL,
    mostSoldItems INTEGER NOT NULL REFERENCES menuItems(id)
);

CREATE TABLE predictions(
    id SERIAL PRIMARY KEY,
    ingredientId INTEGER NOT NULL REFERENCES ingredients(id),
    userId INTEGER NOT NULL REFERENCES users(id),
    predictedUsage FLOAT NOT NULL,
    predictionDate DATE NOT NULL
);

CREATE TYPE change_type AS ENUM ('Used', 'Received', 'Adjusted', 'Losses');

CREATE TABLE inventoryLog(
    id SERIAL PRIMARY KEY,
    ingredientId INTEGER NOT NULL REFERENCES ingredients(id),
    userId INTEGER NOT NULL REFERENCES users(id),
    changeDate TIMESTAMP NOT NULL,
    changeType change_type NOT NULL,
    quantity FLOAT NOT NULL
);
