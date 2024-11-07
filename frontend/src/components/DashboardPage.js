import React from "react";
import { Card, Button, Space, Typography, Row, Col } from "antd";
import { useNavigate } from "react-router-dom";
import { AppstoreOutlined, UnorderedListOutlined, ShoppingCartOutlined } from "@ant-design/icons";

const { Title, Text } = Typography;

const DashboardPage = () => {
    const navigate = useNavigate();

    const handleIngredientsClick = () => {
        navigate("/ingredients");
    };

    const handleOrdersClick = () => {
        navigate("/orders");
    };

    const handleMenuItemsClick = () => {
        navigate("/menu");
    };

    return (
        <div style={{ backgroundColor: "#f0f2f5", minHeight: "100vh", display: "flex", justifyContent: "center", alignItems: "center" }}>
            <Card bordered={false} style={{ width: 500, padding: "20px", boxShadow: "0px 4px 12px rgba(0,0,0,0.1)", borderRadius: "10px" }}>
                <Title level={3} style={{ textAlign: "center", marginBottom: "20px" }}>Dashboard</Title>
                <Text style={{ textAlign: "center", display: "block", marginBottom: "20px", color: "rgba(0, 0, 0, 0.65)" }}>
                    Welcome! Choose one of the options below:
                </Text>
                <Space direction="vertical" style={{ width: "100%" }}>
                    <Button type="primary" icon={<AppstoreOutlined />} block size="large" onClick={handleIngredientsClick}>
                        View All Ingredients
                    </Button>
                    <Button type="primary" icon={<UnorderedListOutlined />} block size="large" onClick={handleOrdersClick}>
                        View All Orders
                    </Button>
                    <Button type="primary" icon={<ShoppingCartOutlined />} block size="large" onClick={handleMenuItemsClick}>
                        View All Menu Items
                    </Button>
                </Space>
            </Card>
        </div>
    );
};

export default DashboardPage;
