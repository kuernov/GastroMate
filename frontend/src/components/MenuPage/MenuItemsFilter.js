import React from "react";
import {Select, InputNumber, Button, Input} from "antd";

const { Option } = Select;

const MenuItemsFilter = ({
                             ingredients,
                             categories,
                             filters,
                             setFilters,
                             fetchFilteredMenuItems,
                             resetFilters
                         }) => {
    return (
        <div>

            <Input
                placeholder="Search by Name"
                onChange={(e) => setFilters((prev) => ({ ...prev, name: e.target.value }))}
                style={{ width: "300px", marginBottom: "10px" }}
                value={filters.name}
            />

            <Select
                mode="multiple"
                placeholder="Select Ingredients"
                onChange={(value) => setFilters((prev) => ({ ...prev, selectedIngredients: value }))}
                style={{ width: "300px", marginBottom: "10px" }}
                value={filters.selectedIngredients}
            >
                {ingredients.map((ingredient) => (
                    <Option key={ingredient.id} value={ingredient.name}>
                        {ingredient.name}
                    </Option>
                ))}
            </Select>

            <Select
                placeholder="Select Category"
                onChange={(value) => setFilters((prev) => ({ ...prev, selectedCategory: value }))}
                style={{ width: "200px", marginRight: "10px" }}
                value={filters.selectedCategory}
            >
                {categories.map((category) => (
                    <Option key={category.id} value={category.name}>
                        {category.name}
                    </Option>
                ))}
            </Select>

            <Select
                placeholder="Select Size"
                onChange={(value) => setFilters((prev) => ({ ...prev, selectedSize: value }))}
                style={{ width: "200px", marginRight: "10px" }}
                value={filters.selectedSize}
            >
                <Option value="S">Small</Option>
                <Option value="M">Medium</Option>
                <Option value="L">Large</Option>
            </Select>

            <InputNumber
                placeholder="Min Price"
                min={0}
                onChange={(value) => setFilters((prev) => ({ ...prev, minPrice: value }))}
                style={{ marginRight: "10px" }}
                value={filters.minPrice}
            />
            <InputNumber
                placeholder="Max Price"
                min={0}
                onChange={(value) => setFilters((prev) => ({ ...prev, maxPrice: value }))}
                value={filters.maxPrice}
            />


            <Button onClick={resetFilters} style={{ marginTop: "10px" }}>
                Clear Filters
            </Button>
        </div>
    );
};

export default MenuItemsFilter;
