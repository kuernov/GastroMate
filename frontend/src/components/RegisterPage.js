import React from "react";
import { Card, Form, Input, Button, message } from "antd";
import { useNavigate } from "react-router-dom";
import { register } from "./services/AuthService";


const RegisterPage = () => {
    const navigate = useNavigate();

    const onFinish = async (values) => {
        try {
            await register(values);
            message.success("Registration successful!");
            navigate("/login"); 
        } catch (error) {
            message.error("An error occurred during registration");
        }
    };

    return (
        <Card title="Register" style={{ width: 300, margin: "auto" }}>
            <Form name="register_form" onFinish={onFinish}>
                <Form.Item
                    name="username"
                    rules={[{ required: true, message: "Please input your Username!" }]}
                >
                    <Input placeholder="Username" />
                </Form.Item>
                <Form.Item
                    name="email"
                    rules={[{ required: true, message: "Please input your Email!" }]}
                >
                    <Input placeholder="Email" />
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{ required: true, message: "Please input your Password!" }]}
                >
                    <Input.Password placeholder="Password" />
                </Form.Item>
                <Form.Item
                    name="street"
                    rules={[{ required: true, message: "Please input your Street!" }]}
                >
                    <Input placeholder="Street" />
                </Form.Item>
                <Form.Item
                    name="city"
                    rules={[{ required: true, message: "Please input your City!" }]}
                >
                    <Input placeholder="City" />
                </Form.Item>
                <Form.Item
                    name="postalCode"
                    rules={[{ required: true, message: "Please input your Postal Code!" }]}
                >
                    <Input placeholder="Postal Code" />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
                        Register
                    </Button>
                </Form.Item>
            </Form>
        </Card>
    );
};

export default RegisterPage;
