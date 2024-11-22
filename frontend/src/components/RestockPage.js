import React, { useState, useEffect } from "react";
import {Table, Tag, Spin, Alert, message} from "antd";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const LowStockIngredients = ({ userId }) => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();


    useEffect(() => {
        // Pobieranie danych z backendu
        const fetchData = async () => {
            try {
                const token = localStorage.getItem("accessToken");
                if (!token) {
                    message.error("User not authenticated");
                    navigate("/login");
                    return;
                }

                const response = await axios.get(`http://localhost:8080/restock/low-stock-items`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setData(response.data.missingItems);
            } catch (err) {
                setError("Nie udało się pobrać danych z serwera.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [navigate]);

    const columns = [
        {
            title: "Product",
            dataIndex: ["ingredient", "name"],
            key: "name",
        },
        {
            title: "Stock Quantity",
            dataIndex: ["ingredient", "quantity"],
            key: "quantity",
            render: (quantity) => <span>{quantity.toFixed(2)}</span>,
        },
        {
            title: "Average Usage",
            dataIndex: "averageUsage",
            key: "averageUsage",
            render: (averageUsage) => <span>{averageUsage.toFixed(2)}</span>,
        },
        {
            title: "Difference",
            dataIndex: "difference",
            key: "difference",
            sorter: (a, b) => a.difference - b.difference,
            render: (difference) => (
                <span style={{ color: difference < 0 ? "red" : "green" }}>
                {difference.toFixed(2)}
            </span>
            ),
        },
        {
            title: "Status",
            dataIndex: "status",
            key: "status",
            render: (status) => {
                let color;
                if (status === "CRITICAL") color = "red";
                else if (status === "LOW_STOCK") color = "orange";
                else color = "green";

                return <Tag color={color}>{status.replace("_", " ")}</Tag>;
            },
            filters: [
                { text: "Critical", value: "CRITICAL" },
                { text: "Low Stock", value: "LOW_STOCK" },
                { text: "Normal", value: "NORMAL" },
            ],
            onFilter: (value, record) => record.status === value,
        },
    ];


    if (loading) return <Spin tip="Ładowanie danych..." />;
    if (error) return <Alert message="Błąd" description={error} type="error" />;

    return (
        <Table
            dataSource={data}
            columns={columns}
            rowKey={(record) => record.ingredient.id}
            title={() => "Produkty poniżej średniego zużycia"}
        />
    );
};

export default LowStockIngredients;
