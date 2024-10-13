import React from "react";
import { Card, Button } from "antd";
import { useNavigate } from "react-router-dom";

const DashboardPage = () => {
    const navigate = useNavigate();

    const handleIngredientsClick = () => {
        // Tutaj później dodasz nawigację do strony składników
        navigate("/ingredients");
    };

    const handleOrdersClick = () => {
        // Tutaj później dodasz nawigację do strony zamówień
        navigate("/orders");
    };

    const handleMenuItemsClick = () => {
        // Tutaj później dodasz nawigację do strony pozycji w menu
        navigate("/menu");
    };

    return (
        <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
            <Card title="Dashboard" style={{ width: 400 }}>
                <p>Welcome! Choose one of the following options:</p>
                <Button type="primary" block style={{ marginBottom: 16 }} onClick={handleIngredientsClick}>
                    View All Ingredients
                </Button>
                <Button type="primary" block style={{ marginBottom: 16 }} onClick={handleOrdersClick}>
                    View All Orders
                </Button>
                <Button type="primary" block style={{ marginBottom: 16 }} onClick={handleMenuItemsClick}>
                    View All Menu Items
                </Button>
            </Card>
        </div>
    );
};

export default DashboardPage;
