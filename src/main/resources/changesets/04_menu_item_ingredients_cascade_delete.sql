ALTER TABLE menu_item_ingredients
DROP CONSTRAINT menu_item_ingredients_ingredient_id_fkey;

ALTER TABLE menu_item_ingredients
    ADD CONSTRAINT menu_item_ingredients_ingredient_id_fkey
        FOREIGN KEY (ingredient_id)
            REFERENCES ingredients(id)
            ON DELETE CASCADE;
