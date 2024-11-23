import React from "react";
import { Card, Form, Input, Button, message } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { login } from "./services/AuthService"; // Import funkcji logowania

const LoginPage = ({ setIsLoggedIn }) => {
    const navigate = useNavigate();

    const onFinish = async (values) => {
        const { username: email, password } = values; // Z Ant Design otrzymujemy obiekt wartości

        try {
            await login(email, password); // Wywołanie funkcji logowania z AuthService
            setIsLoggedIn(true); // Informujemy aplikację o zalogowaniu
            message.success("Logged in successfully!");
            navigate("/dashboard"); // Przekierowanie na stronę główną lub dashboard
        } catch (error) {
            console.error("Login error:", error);
            message.error("Login failed. Please check your credentials.");
        }
    };

    return (
        <Card title="Login" style={{ width: 300, margin: "auto", marginTop: "100px" }}>
            <Form name="login_form" onFinish={onFinish}>
                <Form.Item
                    name="username"
                    rules={[{ required: true, message: "Please input your Email!" }]}
                >
                    <Input prefix={<UserOutlined />} placeholder="Email" />
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{ required: true, message: "Please input your Password!" }]}
                >
                    <Input.Password prefix={<LockOutlined />} placeholder="Password" />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
                        Log in
                    </Button>
                </Form.Item>
                <Form.Item>
                    <Button type="link" onClick={() => navigate("/register")}>
                        Don't have an account? Register
                    </Button>
                </Form.Item>
            </Form>
        </Card>
    );
};

export default LoginPage;
