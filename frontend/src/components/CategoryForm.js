import React from "react";
import { Modal, Form, Input, InputNumber, Button, message } from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const CategoryForm = ({ visible, setShowNewCategoryModal, categories, setCategories }) => {
    const [newCategoryForm] = Form.useForm();
    const navigate = useNavigate();

    const addNewCategory = async (categoryValues) => {
        try {
            const token = localStorage.getItem("accessToken");

            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const response = await axios.post(
                "http://localhost:8080/categories",
                {
                    name: categoryValues.name,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            if (response.status === 200 || response.status === 201) {
                setCategories([...categories, response.data]);
                message.success("New category added successfully!");
                setShowNewCategoryModal(false);
                newCategoryForm.resetFields();
            } else {
                message.error("Unexpected response from server");
            }
        } catch (error) {
            console.error("Error adding new category:", error); // Loguj błąd
            message.error("Failed to add new category");
        }
    };

    return (
        <Modal
            title="Add New Category"
            open={visible}
            onCancel={() => setShowNewCategoryModal(false)}
            footer={null}
        >
            <Form form={newCategoryForm} layout="vertical" onFinish={addNewCategory}>
                <Form.Item
                    name="name"
                    label="Category Name"
                    rules={[{ required: true, message: "Please enter the category name" }]}
                >
                    <Input placeholder="Enter category name" />
                </Form.Item>


                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Add category
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default CategoryForm;
