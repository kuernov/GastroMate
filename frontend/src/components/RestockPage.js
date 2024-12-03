import React, { useState, useEffect } from "react";
import { Table, Card, Row, Col, Spin, Alert, message, Button, Tag, InputNumber } from "antd";
import { useNavigate } from "react-router-dom";
import api from "../api";

const LowStockIngredients = ({ userId }) => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [error, setError] = useState(null);
    const [predictions, setPredictions] = useState([]);
    const [loadingPredictions, setLoadingPredictions] = useState(false);
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [supplyOrders, setSupplyOrders] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.get("/restock/low-stock-items");
                setData(response.data.missingItems);
            } catch (err) {
                setError("Failed to fetch data from server.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [navigate]);

    const handleSupplyOrderChange = (ingredientId, value) => {
        setSupplyOrders((prev) => ({
            ...prev,
            [ingredientId]: value,
        }));
    };

    const handleCreateSupplyOrder = async () => {
        try {
            const orderItems = Object.entries(supplyOrders)
                .filter(([_, quantity]) => quantity > 0) // Filtruj tylko dodatnie wartości
                .map(([ingredientId, quantity]) => ({
                    ingredientId: parseInt(ingredientId, 10),
                    quantity: parseFloat(quantity),
                }));
            if (orderItems.length === 0) {
                message.warning("No items to order.");
                return;
            }
            await api.post("/supply-order", { supplyOrderItemRequestList: orderItems });

            message.success("Supply order created successfully!");
            setSupplyOrders({});
        } catch (error) {
            console.error("Error creating supply order:", error);
            setError("Failed to create supply order.");
        }
    };
    const handleGeneratePredictions = async () => {
        if (selectedIngredients.length === 0) {
            message.warning("Please select at least one ingredient.");
            return;
        }

        try {
            setLoadingPredictions(true);
            const response = await api.post("/predict-selected-ingredients", {
                ingredientIds: selectedIngredients,
            });
            setPredictions(response.data);
            message.success("Predictions generated successfully!");
        } catch (err) {
            setError("Failed to fetch data from server.");
        } finally {
            setLoadingPredictions(false);
        }
    };

    const columns = [
        {
            title: "Product",
            dataIndex: ["ingredient", "name"],
            key: "name",
        },
        {
            title: "Stock Quantity",
            dataIndex: ["quantitySum"],
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
            title: "Order Quantity",
            key: "orderQuantity",
            render: (_, record) => (
                <InputNumber
                    min={0}
                    value={supplyOrders[record.ingredient.id] || 0}
                    onChange={(value) => handleSupplyOrderChange(record.ingredient.id, value)}
                />
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

    if (loading) return <Spin tip="Loading data..." />;
    if (error) return <Alert message="Error" description={error} type="error" />;

    return (
        <div style={{ padding: "20px" }}>
            <h2 style={{ textAlign: "center", marginBottom: "20px" }}>Low Stock Ingredients</h2>
            <Table
                dataSource={data}
                columns={columns}
                rowKey={(record) => record.ingredient.id}
                rowSelection={{
                    onChange: (selectedRowKeys) => setSelectedIngredients(selectedRowKeys),
                }}
                title={() => (
                    <div style={{ display: "flex", justifyContent: "space-between" }}>
                        <Button
                            type="primary"
                            onClick={handleGeneratePredictions}
                            loading={loadingPredictions}
                        >
                            Generate Predictions for Selected Ingredients
                        </Button>
                        <Button
                            type="primary"
                            onClick={handleCreateSupplyOrder}
                        >
                            Create Supply Order
                        </Button>
                    </div>
                )}
                pagination={{ pageSize: 10 }}
            />

            {predictions.length > 0 && (
                <div style={{ padding: "20px", backgroundColor: "#f9f9f9", borderRadius: "10px" }}>
                    <h2 style={{ textAlign: "center", marginBottom: "20px" }}>Predictions for Selected Ingredients</h2>
                    <Row gutter={[16, 16]}>
                        {predictions.map((prediction, index) => {
                            const totalPredicted = prediction.predictedValue.reduce((sum, value) => sum + value, 0).toFixed(2);
                            return (
                                <Col key={index} xs={24} sm={12} md={8} lg={6}>
                                    <Card
                                        title={prediction.ingredientName}
                                        bordered={true}
                                        style={{ boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)" }}
                                    >
                                        <p>
                                            <strong>Total Predicted Value: </strong>
                                            {totalPredicted}
                                        </p>
                                        <Table
                                            dataSource={prediction.date.map((date, idx) => ({
                                                key: idx,
                                                date: new Date(date).toLocaleDateString(),
                                                predictedValue: prediction.predictedValue[idx],
                                            }))}
                                            columns={[
                                                {
                                                    title: "Date",
                                                    dataIndex: "date",
                                                    key: "date",
                                                },
                                                {
                                                    title: "Predicted Value",
                                                    dataIndex: "predictedValue",
                                                    key: "predictedValue",
                                                },
                                            ]}
                                            pagination={false}
                                            size="small"
                                            bordered
                                        />
                                    </Card>
                                </Col>
                            );
                        })}
                    </Row>
                </div>
            )}
        </div>
    );
};

export default LowStockIngredients;
