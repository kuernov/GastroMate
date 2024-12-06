import React, { useState, useEffect } from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
    Link,
    useLocation,
} from "react-router-dom";
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
import { TokenService } from "./components/services/TokenService";
import {logout, refreshAccessToken} from "./components/services/AuthService";

const { Header, Content, Footer } = Layout;

const ProtectedRoute = ({ isLoggedIn, children }) => {
    return isLoggedIn ? children : <Navigate to="/login" />;
};

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(!!TokenService.getToken());

    useEffect(() => {
        document.title = "GastroMate";
    }, []);

    const handleLogout = async () => {
        try {
            await logout();
            setIsLoggedIn(false);
            message.success("Successfully logged out.");
            //window.location.href = "/login";
        } catch (error) {
            console.error("Logout error:", error);
            message.error("Failed to log out.");
        }
    };

    useEffect(() => {
        const checkAndRefreshToken = async () => {
            try {
                // Jeśli accessToken istnieje, odśwież token
                if (TokenService.getToken()) {
                    const newAccessToken = await refreshAccessToken();
                    TokenService.setToken(newAccessToken);
                    setIsLoggedIn(true);
                } else {
                    // Brak accessToken — użytkownik niezalogowany
                    setIsLoggedIn(false);
                }
            } catch (error) {
                console.warn("Failed to refresh access token or no valid session:", error);
                TokenService.clearToken();
                setIsLoggedIn(false);
            }
        };

        checkAndRefreshToken();
    }, []);

    return (
        <Router>
            <Layout className="layout">
                <HeaderComponent isLoggedIn={isLoggedIn} handleLogout={handleLogout}/>
                <Content style={{padding: "50px 50px"}}>
                    <Routes>
                        <Route
                            path="/"
                            element={
                                isLoggedIn ? <Navigate to="/dashboard"/> : <Navigate to="/login"/>
                            }
                        />
                        <Route
                            path="/login"
                            element={<LoginPage setIsLoggedIn={setIsLoggedIn}/>}
                        />
                        <Route path="/register" element={<RegisterPage/>}/>
                        <Route
                            path="/dashboard"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <DashboardPage/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/ingredients"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <IngredientsPage/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/menu"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <MenuItemsPage/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/orders"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <OrdersPage/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/reports"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <SalesReportPage/>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/restock"
                            element={
                                <ProtectedRoute isLoggedIn={isLoggedIn}>
                                    <RestockPage/>
                                </ProtectedRoute>
                            }
                        />
                        <Route path="*" element={<Navigate to="/login"/>}/>
                    </Routes>
                </Content>
                <footer style={{backgroundColor: '#f0f0f0', padding: '20px', textAlign: 'center'}}>
                    <div>
                        <p>© 2024 GastroMate
                        </p>
                        <p>Version: 1.0.0</p>
                    </div>
                </footer>
            </Layout>
        </Router>
    );
};

const HeaderComponent = ({isLoggedIn, handleLogout}) => {
    const location = useLocation();

    // Zdefiniowanie elementów menu jako tablica obiektów
    const menuItems = !isLoggedIn
        ? [
            {key: "/login", label: <Link to="/login">Login</Link>},
            {key: "/register", label: <Link to="/register">Register</Link>},
        ]
        : [
            {key: "/dashboard", label: <Link to="/dashboard">Dashboard</Link>},
            {key: "/ingredients", label: <Link to="/ingredients">Ingredients</Link>},
            {key: "/menu", label: <Link to="/menu">Menu</Link> },
            { key: "/orders", label: <Link to="/orders">Orders</Link> },
            { key: "/reports", label: <Link to="/reports">Sales Reports</Link> },
            { key: "/restock", label: <Link to="/restock">Restock</Link> },
        ];

    return (
        <Header className="header">
            <div className="header-container">
                <div className="logo">GastroMate</div>
                <Menu
                    theme="dark"
                    mode="horizontal"
                    selectedKeys={[location.pathname]}
                    className="menu"
                    items={menuItems} // Użycie nowej właściwości items
                />
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
