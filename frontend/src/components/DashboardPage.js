import React from "react";
import { Card, Button, Space, Typography } from "antd";
import { useNavigate } from "react-router-dom";
import { AppstoreOutlined, UnorderedListOutlined, ShoppingCartOutlined, SyncOutlined, LineChartOutlined } from "@ant-design/icons";

const { Title, Text } = Typography;

const DashboardPage = () => {
    const navigate = useNavigate();

    const buttons = [
        { label: "View All Ingredients", icon: <AppstoreOutlined />, color: "#722ed1", onClick: () => navigate("/ingredients") },
        { label: "View All Orders", icon: <UnorderedListOutlined />, color: "#722ed1", onClick: () => navigate("/orders") },
        { label: "View All Menu Items", icon: <ShoppingCartOutlined />, color: "#722ed1", onClick: () => navigate("/menu") },
        { label: "View Restock", icon: <SyncOutlined />, color: "#722ed1", onClick: () => navigate("/restock") },
        { label: "View Sales Report", icon: <LineChartOutlined />, color: "#722ed1", onClick: () => navigate("/reports") },
    ];

    return (
        <div style={{
            background: "#f0f2f5", // Jednolity kolor tła zamiast gradientu
            minHeight: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            margin: 0,
            padding: 0
        }}>
            <Card
                bordered={false}
                style={{
                    width: 500,
                    padding: "20px",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)", // Subtelniejszy cień
                    borderRadius: "10px",
                    backgroundColor: "#ffffff",
                    margin: "20px", // Dla odstępu od krawędzi
                }}
            >
                <Title level={3} style={{ textAlign: "center", marginBottom: "20px", color: "#af2cfd" }}>
                    Dashboard
                </Title>
                <Text style={{ textAlign: "center", display: "block", marginBottom: "20px", color: "rgba(0, 0, 0, 0.65)" }}>
                    Welcome! Choose one of the options below:
                </Text>
                <Space direction="vertical" style={{ width: "100%" }}>
                    {buttons.map((button, index) => (
                        <Button
                            key={index}
                            type="primary"
                            icon={button.icon}
                            block
                            size="large"
                            onClick={button.onClick}
                            style={{
                                backgroundColor: button.color,
                                borderColor: button.color,
                                height: "60px",
                                fontWeight: "bold",
                                margin: "5px 0" // Odstęp między przyciskami
                            }}
                        >
                            {button.label}
                        </Button>
                    ))}
                </Space>
            </Card>
        </div>
    );
};

export default DashboardPage;
