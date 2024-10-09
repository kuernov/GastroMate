import React from "react";
import {Form, Input, InputNumber, Button, Select, message, Card} from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const IngredientsForm = ({ units, setIngredients, ingredients }) => {
    const [form] = Form.useForm();
    const navigate = useNavigate();

    const onFinish = async (values) => {
        try {
            const token = localStorage.getItem("accessToken");

            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const response = await axios.post(
                "http://localhost:8080/ingredients",
                {
                    name: values.name,
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
        } catch (error) {
            message.error("Failed to add ingredient");
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
                    <Input placeholder="Enter ingredient name" />
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
                    <Select placeholder="Select a unit">
                        {units.map((unit) => (
                            <Select.Option key={unit.id} value={unit.id}>
                                {unit.name}
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item name="expiryDate" label="Expiry Date">
                    <Input type="date" />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Add Ingredient
                    </Button>
                </Form.Item>
            </Form>
        </Card>
    );
};

export default IngredientsForm;
