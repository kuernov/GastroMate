ALTER TABLE menu_item_ingredients
    ADD COLUMN unit_id INT;

ALTER TABLE menu_item_ingredients
    ADD CONSTRAINT fk_menu_item_ingredients_unit_id
        FOREIGN KEY (unit_id) REFERENCES units(id)
            ON DELETE CASCADE;
