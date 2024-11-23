import React, { useState, useEffect } from "react";
import {Table, Card, Row, Col, Spin, Alert, message, Button, Tag} from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const LowStockIngredients = ({ userId }) => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [error, setError] = useState(null);
    const [predictions, setPredictions] = useState([]);
    const [loadingPredictions, setLoadingPredictions] = useState(false);
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
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

    const handleGeneratePredictions = async () => {
        if (selectedIngredients.length === 0) {
            message.warning("Please select at least one ingredient.");
            return;
        }

        try {
            setLoadingPredictions(true);
            const token = localStorage.getItem("accessToken");
            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const response = await axios.post(
                `http://localhost:8080/predict-selected-ingredients`,
                { ingredientIds: selectedIngredients },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            setPredictions(response.data);
            message.success("Predictions generated successfully!");
        } catch (err) {
            message.error("Failed to generate predictions.");
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
                    <Button
                        type="primary"
                        onClick={handleGeneratePredictions}
                        loading={loadingPredictions}
                        style={{ marginBottom: "16px" }}
                    >
                        Generate Predictions for Selected Ingredients
                    </Button>
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
                                            {prediction.totalPredictedValue}
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
