-- 02_add_menu_item_categories.sql
--liquibase formatted sql
--changeset malyjasiak:2

CREATE TABLE menu_item_categories (
    menu_item_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (menu_item_id, category_id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);