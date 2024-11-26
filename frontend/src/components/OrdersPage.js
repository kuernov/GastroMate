import React, { useEffect, useState } from 'react';
import { Table, message, DatePicker } from 'antd';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import moment from 'moment';
import api from "../api";

const { RangePicker } = DatePicker;

const OrdersPage = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [dateRange, setDateRange] = useState([null, null]); // Stores selected start and end dates
    const navigate = useNavigate();

    const fetchData = async (page = 0, startDate, endDate) => {
        try {
            const params = {
                page,
                size: 10,
                startDate: startDate ? startDate.format("YYYY-MM-DD") : undefined,
                endDate: endDate ? endDate.format("YYYY-MM-DD") : undefined,
            };

            const ordersResponse = await api.get("/orders", { params });

            setOrders(ordersResponse.data.content);
            setTotalPages(ordersResponse.data.totalPages);
        } catch (error) {
            message.error("Failed to fetch data");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage, dateRange[0], dateRange[1]);
    }, [navigate, currentPage, dateRange]);

    const handleTableChange = (pagination) => {
        setCurrentPage(pagination.current - 1);
    };

    const handleDateChange = (dates) => {
        setDateRange(dates);
        setCurrentPage(0); // Reset to the first page when changing date filters
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
            render: (text) => moment(text).format("YYYY-MM-DD HH:mm:ss")
        },
        {
            title: 'Items',
            dataIndex: 'orderItems',
            key: 'orderItems',
            render: (items) => (
                <ul>
                    {items.map((item, index) => (
                        <li key={index}>
                            {`${item.menuItemName} (x${item.quantity}) - ${(item.priceAtOrder * item.quantity).toFixed(2)} zł`}
                        </li>
                    ))}
                </ul>
            ),
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
            <RangePicker onChange={handleDateChange} style={{ marginBottom: 20 }} />

            <Table
                columns={columns}
                dataSource={orders}
                rowKey="id"
                loading={loading}
                pagination={{
                    current: currentPage + 1,
                    pageSize: 10,
                    total: totalPages * 10,
                }}
                onChange={handleTableChange}
            />
        </div>
    );
};

export default OrdersPage;
