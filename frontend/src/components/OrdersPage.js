import React, { useEffect, useState } from 'react';
import { Table, Button, Tag, message } from 'antd';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const OrdersPage = () => {
    const [orders, setOrders] = useState([]);
    const [menuItems, setMenuItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0); // Dodano stan do przechowywania bieżącej strony
    const [totalPages, setTotalPages] = useState(0); // Dodano stan do przechowywania liczby stron
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async (page = 0) => {
            try {
                const token = localStorage.getItem("accessToken");
                if (!token) {
                    message.error("User not authenticated");
                    navigate("/login");
                    return;
                }

                const [ordersResponse, menuItemResponse] = await Promise.all([
                    axios.get(`http://localhost:8080/orders?page=${page}&size=10`, {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                    axios.get("http://localhost:8080/menu", {
                        headers: { Authorization: `Bearer ${token}` },
                    })
                ]);

                console.log("Orders Response:", ordersResponse.data); // Dodaj logowanie odpowiedzi
                console.log("Menu Items Response:", menuItemResponse.data);

                setMenuItems(menuItemResponse.data);
                setOrders(ordersResponse.data.content);
                setTotalPages(ordersResponse.data.totalPages);
                setLoading(false);
            } catch (error) {
                message.error("Failed to fetch data");
                setLoading(false);
            }
        };

        fetchData(currentPage);
    }, [navigate, currentPage]);

    const handleTableChange = (pagination) => {
        setCurrentPage(pagination.current - 1); // Ant Design używa numeracji stron od 1, dlatego odejmujemy 1
    };



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
                        {items.map((item, index) => {
                            const menuItemId = item.menuItemId;
                            const menuItem = menuItems.find(menuItem => menuItem.id === menuItemId);
                            return (
                                <li key={index}>
                                    {menuItem ? `${menuItem.name} (x${item.quantity}) - ${(item.priceAtOrder*item.quantity).toFixed(2)} zł` : `Item not found (x${item.quantity})`}
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
            render: (price) => `${price.toFixed(2)} zł`,
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
                pagination={{
                    current: currentPage + 1, // Konwertujemy do numeracji od 1
                    pageSize: 10,
                    total: totalPages * 10, // Liczba stron razy liczba elementów na stronę
                }}
                onChange={handleTableChange}
            />
        </div>
    );
};

export default OrdersPage;
