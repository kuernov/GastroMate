import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, Link, useLocation } from "react-router-dom";
import { Button, Layout, Menu, message } from "antd";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import DashboardPage from "./components/DashboardPage";
import IngredientsPage from "./components/IngredientsPage/IngredientsPage";
import SalesReportPage from "./components/ReportPage/SalesReportPage";
import MenuItemsPage from "./components/MenuPage/MenuItemsPage";
import OrdersPage from "./components/OrdersPage";
import RestockPage from "./components/RestockPage";
import "antd/dist/reset.css";
import "./App.css";
import { jwtDecode } from "jwt-decode";
import { TokenService } from "./components/services/TokenService";


const { Header, Content, Footer } = Layout;

// Component to protect routes
// Component to protect routes
const ProtectedRoute = ({ isLoggedIn, children }) => {
    return isLoggedIn ? children : <Navigate to="/login" />;
};

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(!!TokenService.getToken());

    const handleLogout = async () => {
        try {
            await fetch(`${process.env.REACT_APP_API_URL}/logout`, {
                method: "POST",
                credentials: "include",
            });
            TokenService.clearToken();
            setIsLoggedIn(false);
            message.success("Successfully logged out.");
            window.location.href = "/login";
        } catch (error) {
            console.error("Logout error:", error);
            message.error("Failed to log out.");
        }
    };

    // Automatically log out if the access token is expired
    useEffect(() => {
        const token = TokenService.getToken();
        if (token) {
            try {
                const { exp } = jwtDecode(token);
                if (Date.now() >= exp * 1000) {
                    handleLogout();
                }
            } catch (error) {
                console.error("Error decoding token:", error);
                handleLogout();
            }
        }
    }, [isLoggedIn]);

    return (
        <Router>
            <Layout className="layout">
                <HeaderComponent isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
                <Content style={{ padding: "50px 50px" }}>
                    <Routes>
                        <Route
                            path="/login"
                            element={<LoginPage setIsLoggedIn={setIsLoggedIn} />}
                        />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route
                            path="/dashboard"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <DashboardPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/ingredients"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <IngredientsPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/menu"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <MenuItemsPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/orders"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <OrdersPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/reports"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <SalesReportPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/restock"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <RestockPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route path="*" element={<Navigate to="/login" />} />
                    </Routes>
                </Content>
                <Footer style={{ textAlign: "center" }}>
                    GastroMate ©2024 Created by You
                </Footer>
            </Layout>
        </Router>
    );
};

const HeaderComponent = ({ isLoggedIn, handleLogout }) => {
    const location = useLocation(); // Przenieś tutaj, wewnątrz <Router>

    return (
        <Header className="header">
            <div className="header-container">
                <div className="logo">GastroMate</div>
                <Menu
                    theme="dark"
                    mode="horizontal"
                    selectedKeys={[location.pathname]}
                    className="menu"
                >
                    {!isLoggedIn ? (
                        <>
                            <Menu.Item key="/login">
                                <Link to="/login">Login</Link>
                            </Menu.Item>
                            <Menu.Item key="/register">
                                <Link to="/register">Register</Link>
                            </Menu.Item>
                        </>
                    ) : (
                        <>
                            <Menu.Item key="/dashboard">
                                <Link to="/dashboard">Dashboard</Link>
                            </Menu.Item>
                            <Menu.Item key="/ingredients">
                                <Link to="/ingredients">Ingredients</Link>
                            </Menu.Item>
                            <Menu.Item key="/menu">
                                <Link to="/menu">Menu</Link>
                            </Menu.Item>
                            <Menu.Item key="/orders">
                                <Link to="/orders">Orders</Link>
                            </Menu.Item>
                            <Menu.Item key="/reports">
                                <Link to="/reports">Sales Reports</Link>
                            </Menu.Item>
                            <Menu.Item key="/restock">
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
    );
};

export default App;
