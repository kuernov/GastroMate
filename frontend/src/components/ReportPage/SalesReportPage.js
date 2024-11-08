import React, { useState } from 'react';
import { DatePicker, Button, message } from 'antd';
import CategoryRevenueChart from './CategoryRevenueChart';
import CategoryQuantityChart from './CategoryQuantityChart';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const SalesReportPage = () => {
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [totalRevenue, setTotalRevenue] = useState(0);
    const [categoryRevenue, setCategoryRevenue] = useState([]);
    const [topSellingItems, setTopSellingItems] = useState([]);
    const [ordersCount, setOrdersCount] = useState(0);
    const [averageOrderValue, setAverageOrderValue] = useState(0);
    const navigate = useNavigate();

    const handleGenerateReport = () => {
        const formattedStart = startDate.format('YYYY-MM-DD');
        const formattedEnd = endDate.format('YYYY-MM-DD');
        fetchReportData(formattedStart, formattedEnd);
    };

    const fetchReportData = async (startDate, endDate) => {
        const token = localStorage.getItem("accessToken");
        if (!token) {
            message.error("User not authenticated");
            navigate("/login");
            return;
        }

        try {
            const headers = { Authorization: `Bearer ${token}` };
            const requests = [
                axios.get(`http://localhost:8080/reports/total-revenue`, { params: { startDate, endDate }, headers }),
                axios.get(`http://localhost:8080/reports/category-revenue`, { params: { startDate, endDate }, headers }),
                axios.get(`http://localhost:8080/reports/orders-count`, { params: { startDate, endDate }, headers }),
                axios.get(`http://localhost:8080/reports/average-order-value`, { params: { startDate, endDate }, headers }),
                axios.get(`http://localhost:8080/reports/top-selling-items`, { params: { startDate, endDate }, headers })
            ];

            const [totalRevenueRes, categoryRevenueRes, ordersCountRes, averageOrderValueRes, topSellingItemsRes] = await Promise.all(requests);

            setTotalRevenue(totalRevenueRes.data);
            setCategoryRevenue(categoryRevenueRes.data);
            setOrdersCount(ordersCountRes.data);
            setAverageOrderValue(averageOrderValueRes.data);
            setTopSellingItems(topSellingItemsRes.data);
        } catch (error) {
            console.error("Error fetching report data", error);
            message.error("Failed to fetch report data");
        }
    };

    return (
        <div>
            <h2>Sales Report</h2>
            <DatePicker.RangePicker
                onChange={(dates) => {
                    setStartDate(dates ? dates[0] : null);
                    setEndDate(dates ? dates[1] : null);
                }}
            />
            <Button type="primary" onClick={handleGenerateReport} disabled={!startDate || !endDate} style={{ marginTop: 16 }}>
                Generate Report
            </Button>

            <div style={{ marginTop: 20 }}>
                <h3>Summary</h3>
                <p>Total Revenue: ${totalRevenue}</p>
                <p>Orders Count: {ordersCount}</p>
                <p>Average Order Value: ${averageOrderValue}</p>
            </div>

            <div style={{ display: 'flex', gap: '20px', marginTop: '20px' }}>
                {/* Wykres przychodu według kategorii */}
                <div style={{ flex: 1 }}>
                    <h3>Revenue by Category</h3>
                    <CategoryRevenueChart data={categoryRevenue} />
                </div>

                {/* Wykres ilości według kategorii */}
                <div style={{ flex: 1 }}>
                    <h3>Quantity by Category</h3>
                    <CategoryQuantityChart data={categoryRevenue} />
                </div>
            </div>
        </div>
    );
};

export default SalesReportPage;
