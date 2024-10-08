import React, {useEffect, useState} from "react";
import { Card, Form, Input, Button, message } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

const LoginPage = ({ setIsLoggedIn }) => {
    const navigate = useNavigate();
    const [accessToken, setAccessToken] = useState('');

    const onFinish = async (values) => {
        try {
            const response = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: values.username, // Zakładamy, że 'username' to email
                    password: values.password,
                }),
            });

            if (response.ok) {
                message.success("Login successful!");
                const responseData = await response.json();
                console.log('Odpowiedź od serwera:', responseData);
                const token = responseData.token;
                setIsLoggedIn(true); // Aktualizacja stanu logowania
                setAccessToken(token);
                // Przechowywanie tokenu w localStorage
                localStorage.setItem('accessToken', token);
                console.log('Bearer Token:', token);
                navigate("/dashboard");
            } else {
                message.error("Invalid username or password");
            }
        } catch (error) {
            message.error("An error occurred during login");
        }
    };

    return (
        <Card title="Login" style={{ width: 300, margin: "auto" }}>
            <Form name="login_form" onFinish={onFinish}>
                <Form.Item
                    name="username"
                    rules={[{ required: true, message: "Please input your Username!" }]}
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
