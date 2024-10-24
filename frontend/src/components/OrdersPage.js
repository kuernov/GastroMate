import React, { useEffect, useState } from 'react';
import { Table, Button, Tag, message } from 'antd';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const OrdersPage = () => {
    const [orders, setOrders] = useState([]);
    const [menuItems, setMenuItems] = useState([]);
    const [loading, setLoading] = useState(true);
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

                const [ordersResponse, menuItemResponse] = await Promise.all([
                    axios.get("http://localhost:8080/orders", {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                    axios.get("http://localhost:8080/menu", {
                        headers: { Authorization: `Bearer ${token}` },
                    })
                ]);

                setMenuItems(menuItemResponse.data);
                setOrders(ordersResponse.data);
                setLoading(false);
            } catch (error) {
                message.error("Failed to fetch data");
                setLoading(false);
            }
        };

        fetchData();
    }, [navigate]);

    const columns = [
        {
            title: 'Order ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Order Date',
            dataIndex: 'orderDate',
            key: 'orderDate',
            render: (text) => {
                const date = new Date(text);
                return date.toLocaleString();
            }
        },
        {
            title: 'Items',
            dataIndex: 'orderItems',
            key: 'orderItems',
            render: (items) => {
                return (
                    <ul>
                        {items.map(item => {
                            const menuItemId = item.menuItemId;
                            const menuItem = menuItems.find(menuItem => menuItem.id === menuItemId);
                            return (
                                <li key={menuItemId}>
                                    {menuItem ? `${menuItem.name} (x${item.quantity})` : `Item not found (x${item.quantity})`}
                                </li>
                            );
                        })}
                    </ul>
                );
            },
        },

        {
            title: 'Total Price',
            dataIndex: 'totalPrice',
            key: 'totalPrice',
            render: (price) => `${price.toFixed(2)} zł`, // Wyświetlanie ceny z 2 miejscami po przecinku
        }
    ];

    return (
        <div>
            <h2>Orders</h2>
            <Table
                columns={columns}
                dataSource={orders}
                rowKey="id"
                loading={loading}
                pagination={{ pageSize: 10 }}
            />
        </div>
    );
};

export default OrdersPage;
