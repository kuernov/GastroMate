import React, { useState } from "react";
import { Form, Input, InputNumber, Button, Select, message, Card, Space } from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { PlusOutlined } from "@ant-design/icons";
import CategoryForm from "../CategoryForm";
import api from "../../api";

const MenuItemsForm = ({ ingredients, units, categories, setCategories, menuItems, setMenuItems }) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [showNewCategoryModal, setShowNewCategoryModal] = useState(false);

    const navigate = useNavigate();

    const onFinish = async (values) => {
        try {
            setLoading(true);

            const response = await api.post("/menu", {
                menuItem: {
                    name: values.name,
                    description: values.description,
                    price: values.price,
                    categoryIds: values.categoryIds,
                },
                menuItemIngredients: values.menuItemIngredients.map((ingredient) => ({
                    ingredientId: ingredient.ingredient,
                    quantityRequired: ingredient.quantityRequired,
                    unitId: ingredient.unit,
                })),
            });

            setMenuItems([...menuItems, response.data]);
            message.success("Menu item added successfully!");
            form.resetFields();
        } catch (error) {
            console.error("Failed to add menu item:", error);
            message.error("Failed to add menu item");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card title="Add New Menu Item" style={{ marginTop: "20px" }}>
            <Form form={form} layout="vertical" onFinish={onFinish}>
                <Form.Item
                    name="name"
                    label="Menu Item Name"
                    rules={[{ required: true, message: "Please enter the menu item name" }]}
                >
                    <Input placeholder="Enter menu item name" />
                </Form.Item>

                <Form.Item
                    name="description"
                    label="Description"
                    rules={[{ required: true, message: "Please enter the description" }]}
                >
                    <Input placeholder="Enter description" />
                </Form.Item>

                <Form.Item
                    name="price"
                    label="Price"
                    rules={[{ required: true, message: "Please enter the price" }]}
                >
                    <InputNumber placeholder="Enter price" style={{ width: "100%" }} />
                </Form.Item>

                {/* Nowe pole do wyboru kategorii */}
                <Form.Item
                    name="categoryIds"
                    label="Categories"
                    rules={[{ required: true, message: "Please select at least one category" }]}
                >
                    <Select
                        mode="multiple"
                        placeholder="Select categories"
                        dropdownRender={(menu) => (
                            <>
                                {menu}
                                <Button
                                    type="link"
                                    style={{ display: "block", marginTop: "10px", textAlign: "center" }}
                                    onClick={() => setShowNewCategoryModal(true)}
                                >
                                    Add New Category
                                </Button>
                            </>
                        )}
                    >
                        {categories.map((category) => (
                            <Select.Option key={category.id} value={category.id}>
                                {category.name}
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>



                <Card title="Add Ingredients">
                    <Space direction="vertical" style={{ width: "100%" }}>
                        <Form.List
                            name="menuItemIngredients"
                            initialValue={[]}
                            rules={[
                                {
                                    validator: async (_, ingredients) => {
                                        if (!ingredients || ingredients.length < 1) {
                                            return Promise.reject(new Error("At least one ingredient must be added"));
                                        }
                                    },
                                },
                            ]}
                        >
                            {(fields, { add, remove }) => (
                                <>
                                    {fields.map(({ key, name }) => (
                                        <Space key={key} style={{ display: "flex", marginBottom: 8 }} align="baseline">
                                            <Form.Item
                                                name={[name, "ingredient"]}
                                                rules={[{ required: true, message: "Please select an ingredient" }]}
                                            >
                                                <Select placeholder="Select ingredient" showSearch>
                                                    {ingredients.map((ingredient) => (
                                                        <Select.Option key={ingredient.id} value={ingredient.id}>
                                                            {ingredient.name}
                                                        </Select.Option>
                                                    ))}
                                                </Select>
                                            </Form.Item>

                                            <Form.Item
                                                name={[name, "unit"]}
                                                rules={[{ required: true, message: "Please select a unit" }]}
                                            >
                                                <Select placeholder="Select unit" showSearch>
                                                    {units.map((unit) => (
                                                        <Select.Option key={unit.id} value={unit.id}>
                                                            {unit.name}
                                                        </Select.Option>
                                                    ))}
                                                </Select>
                                            </Form.Item>
                                            <Form.Item
                                                name={[name, "quantityRequired"]}
                                                rules={[{ required: true, message: "Please enter required quantity" }]}
                                            >
                                                <InputNumber placeholder="Enter quantity" style={{ width: "100%" }} />
                                            </Form.Item>

                                            <Button type="danger" onClick={() => remove(name)}>
                                                Remove
                                            </Button>
                                        </Space>
                                    ))}

                                    <Form.Item>
                                        <Button type="dashed" onClick={() => add()} icon={<PlusOutlined />}>
                                            Add Ingredient
                                        </Button>
                                    </Form.Item>
                                </>
                            )}
                        </Form.List>
                    </Space>
                </Card>

                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Add Menu Item
                    </Button>
                </Form.Item>
            </Form>
            <CategoryForm
                visible={showNewCategoryModal}
                setShowNewCategoryModal={setShowNewCategoryModal}
                categories={categories}
                setCategories={setCategories}
            />
        </Card>
    );
};

export default MenuItemsForm;
