import React, { useState, useEffect } from "react";
import {Modal, List, Table, Card, Row, Col, Spin, Alert, message, Button, Tag, InputNumber, Collapse } from "antd";
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
    const [lastOrders, setLastOrders] = useState([]); // Stan dla ostatnich zamówień
    const [isModalVisible, setIsModalVisible] = useState(false); // Stan widoczności modalnego okienka
    const navigate = useNavigate();
    const { Panel } = Collapse;

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

    const fetchLastOrders = async () => {
        try {
            const response = await api.get("/supply-order/last-orders");
            setLastOrders(response.data);
        } catch (err) {
            message.error("Failed to fetch last orders.");
        }
    };

    const showLastOrders = async () => {
        await fetchLastOrders();
        setIsModalVisible(true);
    };

    const closeLastOrdersModal = () => {
        setIsModalVisible(false);
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
        <div style={{padding: "20px"}}>
            <h2 style={{textAlign: "center", fontSize: "24px", fontWeight: "bold", marginBottom: "20px"}}>
                Low Stock Ingredients
            </h2>
            <Table
                dataSource={data}
                columns={columns}
                rowKey={(record) => record.ingredient.id}
                rowSelection={{
                    onChange: (selectedRowKeys) => setSelectedIngredients(selectedRowKeys),
                }}
                title={() => (
                    <div style={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                        <Button
                            type="primary"
                            onClick={handleGeneratePredictions}
                            loading={loadingPredictions}
                            style={{backgroundColor: "#1890ff", color: "white", borderRadius: "5px"}}
                        >
                            Generate Predictions
                        </Button>
                        <Button
                            type="primary"
                            onClick={handleCreateSupplyOrder}
                            style={{backgroundColor: "#52c41a", color: "white", borderRadius: "5px"}}
                        >
                            Create Supply Order
                        </Button>
                        <Button
                            onClick={showLastOrders}
                            style={{
                                border: "1px solid #d9d9d9",
                                borderRadius: "5px",
                                padding: "5px 15px",
                            }}
                        >
                            Show Last Orders
                        </Button>
                    </div>
                )}
                pagination={{pageSize: 10}}
            />

            {predictions.length > 0 && (
                <div style={{padding: "20px", backgroundColor: "#f9f9f9", borderRadius: "10px"}}>
                    <h2 style={{textAlign: "center", marginBottom: "20px"}}>Predictions for Selected Ingredients</h2>
                    <Row gutter={[16, 16]}>
                        {predictions.map((prediction, index) => {
                            const totalPredicted = prediction.predictedValue.reduce((sum, value) => sum + value, 0).toFixed(2);
                            return (
                                <Col key={index} xs={24} sm={12} md={8} lg={6}>
                                    <Card
                                        title={prediction.ingredientName}
                                        bordered={true}
                                        style={{boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)"}}
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
            <Modal
                title="Last 10 Supply Orders"
                visible={isModalVisible}
                onCancel={closeLastOrdersModal}
                footer={[
                    <Button key="close" onClick={closeLastOrdersModal}>
                        Close
                    </Button>,
                ]}
            >
                <List
                    dataSource={lastOrders}
                    renderItem={(order) => (
                        <List.Item>
                            <div>
                                <strong>Order ID:</strong> {order.id}
                            </div>
                            <div>
                                <strong>Date:</strong> {new Date(order.orderDate).toLocaleDateString()}
                            </div>
                            <div>
                                <strong>Status:</strong> {order.status}
                            </div>
                            <div style={{marginTop: "10px"}}>
                                <Collapse>
                                    <Panel header="Ingredients" key={order.id}>
                                        <List
                                            dataSource={order.orderItems}
                                            renderItem={(item) => (
                                                <List.Item>
                                                    <strong>{item.name}</strong> - Quantity: {item.quantity}
                                                </List.Item>
                                            )}
                                        />
                                    </Panel>
                                </Collapse>
                            </div>
                        </List.Item>
                    )}
                />
            </Modal>
        </div>
    );
};

export default LowStockIngredients;
