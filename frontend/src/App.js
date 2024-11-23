import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from "react-router-dom";
import { Button, Layout, Menu } from "antd";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import DashboardPage from "./components/DashboardPage";
import IngredientsPage from "./components/IngredientsPage/IngredientsPage";
import SalesReportPage from "./components/ReportPage/SalesReportPage";
import MenuItemsPage from "./components/MenuPage/MenuItemsPage";
import OrdersPage from "./components/OrdersPage";
import "antd/dist/reset.css";
import "./App.css";
import RestockPage from "./components/RestockPage"; // Dodaj niestandardowe style

const { Header, Content, Footer } = Layout;

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("accessToken"));

    const handleLogout = () => {
        localStorage.removeItem("accessToken");
        setIsLoggedIn(false);
        window.location.href = "/login";
    };

    return (
        <Router>
            <Layout className="layout">
                <Header className="header">
                    <div className="header-container">
                        <div className="logo">GastroMate</div>
                        <Menu theme="dark" mode="horizontal" defaultSelectedKeys={["1"]} className="menu">
                            {!isLoggedIn ? (
                                <>
                                    <Menu.Item key="login">
                                        <Link to="/login">Login</Link>
                                    </Menu.Item>
                                    <Menu.Item key="register">
                                        <Link to="/register">Register</Link>
                                    </Menu.Item>
                                </>
                            ) : (
                                <>
                                    <Menu.Item key="dashboard">
                                        <Link to="/dashboard">Dashboard</Link>
                                    </Menu.Item>
                                    <Menu.Item key="ingredients">
                                        <Link to="/ingredients">Ingredients</Link>
                                    </Menu.Item>
                                    <Menu.Item key="menu">
                                        <Link to="/menu">Menu</Link>
                                    </Menu.Item>
                                    <Menu.Item key="orders">
                                        <Link to="/orders">Orders</Link>
                                    </Menu.Item>
                                    <Menu.Item key="reports">
                                        <Link to="/reports">Sales Reports</Link>
                                    </Menu.Item>
                                    <Menu.Item key="restock">
                                        <Link to="/restock">Restock</Link>
                                    </Menu.Item>
                                </>
                            )}
                        </Menu>
                        {isLoggedIn && (
                            <Button
                                type="primary"
                                danger
                                onClick={handleLogout}
                                className="logout-button"
                            >
                                Logout
                            </Button>
                        )}
                    </div>
                </Header>

                <Content style={{ padding: "50px 50px" }}>
                    <Routes>
                        <Route path="/login" element={<LoginPage setIsLoggedIn={setIsLoggedIn} />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/dashboard" element={<DashboardPage />} />
                        <Route path="/ingredients" element={<IngredientsPage />} />
                        <Route path="/menu" element={<MenuItemsPage />} />
                        <Route path="/orders" element={<OrdersPage />} />
                        <Route path="/reports" element={<SalesReportPage />} />
                        <Route path="/restock" element={<RestockPage />} />
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
