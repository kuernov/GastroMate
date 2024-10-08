import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { Button, Layout, Menu } from "antd";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import DashboardPage from "./components/DashboardPage";
import IngredientsPage from "./components/IngredientsPage";

import "antd/dist/reset.css";

const { Header, Content, Footer } = Layout;

const App = () => {
    // Stan logowania użytkownika
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // Funkcja obsługująca wylogowanie
    const handleLogout = () => {
        setIsLoggedIn(false);
        localStorage.removeItem("accessToken");

        // Tutaj możesz też usunąć token sesji lub wyczyścić dane z localStorage
    };

    return (
        <Router>
            <Layout className="layout">
                <Header>
                    <Menu theme="dark" mode="horizontal" defaultSelectedKeys={["1"]}>
                        {!isLoggedIn ? (
                            <>
                                <Menu.Item key="login">
                                    <a href="/login">Login</a>
                                </Menu.Item>
                                <Menu.Item key="register">
                                    <a href="/register">Register</a>
                                </Menu.Item>
                            </>
                        ) : (
                            <>
                                <Menu.Item key="dashboard">
                                    <a href="/dashboard">Dashboard</a>
                                </Menu.Item>
                                <Menu.Item key="ingredients">
                                    <a href="/ingredients">Ingredients</a>
                                </Menu.Item>
                                <Menu.Item key="logout">
                                    <Button type="primary" onClick={handleLogout}>
                                        Logout
                                    </Button>
                                </Menu.Item>
                            </>
                        )}
                    </Menu>
                </Header>

                <Content style={{ padding: "50px 50px" }}>
                    <Routes>
                        <Route path="/login" element={<LoginPage setIsLoggedIn={setIsLoggedIn} />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/dashboard" element={isLoggedIn ? <DashboardPage /> : <Navigate to="/login" />} />
                        <Route path="/ingredients" element={isLoggedIn ? <IngredientsPage /> : <Navigate to="/login" />} />
                        <Route path="*" element={<Navigate to="/login" />} />
                    </Routes>
                </Content>

                <Footer style={{ textAlign: "center" }}>
                    Ant Design Example ©2024 Created by You
                </Footer>
            </Layout>
        </Router>
    );
};

export default App;
