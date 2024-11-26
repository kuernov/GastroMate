import React from "react";
import { Select, InputNumber, Button, Input, Row, Col } from "antd";

const { Option } = Select;

const MenuItemsFilter = ({
                             ingredients,
                             categories,
                             filters,
                             setFilters,
                             resetFilters,
                         }) => {
    const updateFilter = (key) => (value) =>
        setFilters((prev) => ({ ...prev, [key]: value }));

    return (
        <div style={{ padding: "20px", backgroundColor: "#f9f9f9", borderRadius: "8px" }}>
            <Row gutter={[16, 16]} align="middle" style={{ justifyContent: "center" }}>
                <Col span={6}>
                    <Input
                        placeholder="Search by Name"
                        onChange={(e) => updateFilter("name")(e.target.value)}
                        value={filters.name}
                        style={{ width: "100%" }}
                    />
                </Col>

                <Col span={6}>
                    <Select
                        mode="multiple"
                        placeholder="Select Ingredients"
                        onChange={updateFilter("selectedIngredients")}
                        value={filters.selectedIngredients}
                        style={{ width: "100%" }}
                    >
                        {ingredients.map((ingredient) => (
                            <Option key={ingredient.id} value={ingredient.name}>
                                {ingredient.name}
                            </Option>
                        ))}
                    </Select>
                </Col>

                <Col span={4}>
                    <Select
                        placeholder="Select Category"
                        onChange={updateFilter("selectedCategory")}
                        value={filters.selectedCategory}
                        style={{ width: "100%" }}
                    >
                        {categories.map((category) => (
                            <Option key={category.id} value={category.name}>
                                {category.name}
                            </Option>
                        ))}
                    </Select>
                </Col>

                <Col span={4}>
                    <Select
                        placeholder="Select Size"
                        onChange={updateFilter("selectedSize")}
                        value={filters.selectedSize}
                        style={{ width: "100%" }}
                    >
                        <Option value="S">Small</Option>
                        <Option value="M">Medium</Option>
                        <Option value="L">Large</Option>
                        <Option value="XL">X Large</Option>
                        <Option value="XXL">XX Large</Option>
                    </Select>
                </Col>

                <Col span={2}>
                    <InputNumber
                        placeholder="Min Price"
                        min={0}
                        onChange={updateFilter("minPrice")}
                        value={filters.minPrice}
                        style={{ width: "100%" }}
                    />
                </Col>

                <Col span={2}>
                    <InputNumber
                        placeholder="Max Price"
                        min={0}
                        onChange={updateFilter("maxPrice")}
                        value={filters.maxPrice}
                        style={{ width: "100%" }}
                    />
                </Col>

                <Col span={2}>
                    <Button
                        onClick={resetFilters}
                        type="primary"
                        style={{ width: "100%" }}
                    >
                        Clear
                    </Button>
                </Col>
            </Row>
        </div>
    );
};

export default MenuItemsFilter;
