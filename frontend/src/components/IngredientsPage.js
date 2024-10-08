import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table, Card, message, Form, Input, InputNumber, Button, Select } from "antd";
import { useNavigate } from "react-router-dom";

const IngredientsPage = () => {
    const [ingredients, setIngredients] = useState([]);
    const [units, setUnits] = useState([]); // To store units for the form dropdown
    const [loading, setLoading] = useState(true);
    const [form] = Form.useForm(); // Create a form instance
    const navigate = useNavigate();

    // Fetch ingredients and units on component mount
    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = localStorage.getItem("accessToken");

                if (!token) {
                    message.error("User not authenticated");
                    navigate("/login");
                    return;
                }

                const [ingredientsResponse, unitsResponse] = await Promise.all([
                    axios.get("http://localhost:8080/ingredients", {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                    axios.get("http://localhost:8080/units", {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                ]);
                console.log(ingredientsResponse.data)
                setIngredients(ingredientsResponse.data);
                setUnits(unitsResponse.data); // Store units for dropdown selection
                setLoading(false);
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    message.error("Unauthorized, please log in again.");
                    navigate("/login");
                } else {
                    message.error("Failed to fetch data");
                }
                setLoading(false);
            }
        };

        fetchData();
    }, [navigate]);

    const getUnitNameById = (unitId) => {
        const unit = units.find(u => u.id === unitId);
        return unit ? unit.name : "Unknown Unit";
    };

    // Handle form submission to add a new ingredient
    const onFinish = async (values) => {
        try {
            const token = localStorage.getItem("accessToken");

            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            // Send POST request to add a new ingredient
            const response = await axios.post(
                "http://localhost:8080/ingredients",
                {
                    name: values.name,
                    quantity: values.quantity,
                    unitId: values.unit , // Only send the unit ID
                    expiryDate: values.expiryDate,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            console.log(response.data);  // <-- Log the data here

            // Add the new ingredient to the state (optimistic update)
            setIngredients([...ingredients, response.data]);
            message.success("Ingredient added successfully!");

            // Reset the form after successful submission
            form.resetFields();
        } catch (error) {
            message.error("Failed to add ingredient");
        }
    };

    // Define the table columns
    const columns = [
        {
            title: "ID",
            dataIndex: "id",
            key: "id",
        },
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
        },
        {
            title: "Quantity",
            dataIndex: "quantity",
            key: "quantity",
        },
        {
            title: "Unit",
            dataIndex: "unit",
            key: "unit",
            render: (text, record) => getUnitNameById(record.unitId), // Map unitId to unit name
        },
        {
            title: "Expiry Date",
            dataIndex: "expiryDate",
            key: "expiryDate",
            render: (date) => date ? new Date(date).toLocaleDateString() : 'N/A',
        },
    ];

    return (
        <div style={{ padding: "50px" }}>
            <Card title="Add New Ingredient">
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={onFinish}
                >
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

                    <Form.Item
                        name="expiryDate"
                        label="Expiry Date"
                    >
                        <Input type="date" />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit">
                            Add Ingredient
                        </Button>
                    </Form.Item>
                </Form>
            </Card>

            <Card title="Your Ingredients" style={{ marginTop: "30px" }}>
                <Table
                    dataSource={ingredients}
                    columns={columns}
                    rowKey="id"
                    loading={loading}
                />
            </Card>
        </div>
    );
};

export default IngredientsPage;
