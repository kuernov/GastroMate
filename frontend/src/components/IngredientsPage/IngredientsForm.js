import React, { useState } from "react";
import {Form, InputNumber, Button, Select, AutoComplete, message, Card, Input} from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import UnitForm from "../UnitForm";

const IngredientsForm = ({ units, setIngredients, ingredients, setUnits }) => {
    const [form] = Form.useForm();
    const [showNewUnitModal, setShowNewUnitModal] = useState(false);
    const [loading, setLoading] = useState(false);
    const [ingredientName, setIngredientName] = useState(""); // Trzyma wpisaną nazwę
    const navigate = useNavigate();

    const onFinish = async (values) => {
        try {
            setLoading(true);
            const token = localStorage.getItem("accessToken");

            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            // Wysyłanie danych składnika do backendu
            const response = await axios.post(
                "http://localhost:8080/ingredients",
                {
                    name: ingredientName || values.name,
                    quantity: values.quantity,
                    unitId: values.unit,
                    expiryDate: values.expiryDate,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            setIngredients([...ingredients, response.data]);
            message.success("Ingredient added successfully!");
            form.resetFields();
            setIngredientName(""); // Wyczyść niestandardową nazwę po dodaniu
        } catch (error) {
            message.error("Failed to add ingredient");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card title="Add New Ingredient" style={{ marginTop: "20px" }}>
            <Form form={form} layout="vertical" onFinish={onFinish}>
                <Form.Item
                    name="name"
                    label="Ingredient Name"
                    rules={[{ required: true, message: "Please enter the ingredient name" }]}
                >
                    <AutoComplete
                        options={ingredients.map((ingredient) => ({ value: ingredient.name }))}
                        placeholder="Select or enter an ingredient"
                        onChange={(value) => setIngredientName(value)} // Aktualizacja niestandardowej nazwy
                        value={ingredientName} // Ustawienie wartości AutoComplete na `ingredientName`
                        filterOption={(inputValue, option) =>
                            option.value.toLowerCase().includes(inputValue.toLowerCase())
                        }
                    />
                </Form.Item>

                <Form.Item
                    name="quantity"
                    label="Quantity"
                    rules={[{ required: true, message: "Please enter the quantity" }]}
                >
                    <InputNumber placeholder="Enter quantity" style={{ width: "100%" }} />
                </Form.Item>

                <Form.Item
                    name="unit"
                    label="Unit"
                    rules={[{ required: true, message: "Please select a unit" }]}
                >
                    <Select
                        placeholder="Select a unit"
                        dropdownRender={(menu) => (
                            <>
                                {menu}
                                <Button
                                    type="link"
                                    style={{ display: "block", marginTop: "10px", textAlign: "center" }}
                                    onClick={() => setShowNewUnitModal(true)}
                                >
                                    Add New Unit
                                </Button>
                            </>
                        )}
                    >
                        {units.map((unit) => (
                            <Select.Option key={unit.id} value={unit.id}>
                                {unit.name} ({unit.abbreviation})
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item name="expiryDate" label="Expiry Date">
                    <Input type="date" />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Add Ingredient
                    </Button>
                </Form.Item>
            </Form>

            <UnitForm
                visible={showNewUnitModal}
                setShowNewUnitModal={setShowNewUnitModal}
                units={units}
                setUnits={setUnits}
            />
        </Card>
    );
};

export default IngredientsForm;
