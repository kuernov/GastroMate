ALTER TABLE inventoryLog RENAME COLUMN change_date TO timestamp;

ALTER TABLE inventoryLog RENAME COLUMN quantity TO quantity_change;

ALTER TABLE inventoryLog ADD COLUMN unit_id INTEGER REFERENCES units(id);