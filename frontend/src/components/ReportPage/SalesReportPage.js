import React, { useState } from 'react';
import {DatePicker, Button, message, Col, Card, Statistic, Row} from 'antd';
import CategoryRevenueChart from './CategoryRevenueChart';
import CategoryQuantityChart from './CategoryQuantityChart';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import WeeklySalesRevenueChart from "./WeeklySalesRevenueChart";
import WeeklySalesQuantityChart from "./WeeklySalesQuantityChart";
import TopSellingTable from "./TopSellingTable";
import { Spin } from 'antd';
import CustomLineChart from "./CustomLineChart";
import api from "../../api";


const SalesReportPage = () => {
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [totalRevenue, setTotalRevenue] = useState(0);
    const [categoryRevenue, setCategoryRevenue] = useState([]);
    const [topSellingItems, setTopSellingItems] = useState([]);
    const [salesByDayOfWeek, setSalesByDayOfWeek] = useState([]);
    const [salesByHour, setSalesByHour] = useState([]);
    const [ordersCount, setOrdersCount] = useState(0);
    const [averageOrderValue, setAverageOrderValue] = useState(0);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleGenerateReport = () => {
        setLoading(true);
        const formattedStart = startDate.format('YYYY-MM-DD');
        const formattedEnd = endDate.format('YYYY-MM-DD');
        fetchReportData(formattedStart, formattedEnd);
    };

    const fetchReportData = async (startDate, endDate) => {
        try {
            const requests = [
                await api.get(`/reports/total-revenue`, { params: { startDate, endDate } }),
                await api.get(`/reports/category-revenue`, { params: { startDate, endDate } }),
                await api.get(`/reports/orders-count`, { params: { startDate, endDate } }),
                await api.get(`/reports/average-order-value`, { params: { startDate, endDate } }),
                await api.get(`/reports/top-selling-items`, { params: { startDate, endDate } }),
                await api.get(`/reports/sales-by-day-of-week`, { params: { startDate, endDate } }),
                await api.get(`/reports/sales-by-hour`, { params: { startDate, endDate } })
            ];

            const [
                totalRevenueRes,
                categoryRevenueRes,
                ordersCountRes,
                averageOrderValueRes,
                topSellingItemsRes,
                salesByDayRes,
                salesByHourRes
            ] = await Promise.all(requests);

            setTotalRevenue(totalRevenueRes.data);
            setCategoryRevenue(categoryRevenueRes.data);
            setOrdersCount(ordersCountRes.data);
            setAverageOrderValue(averageOrderValueRes.data);
            setTopSellingItems(topSellingItemsRes.data);
            setSalesByDayOfWeek(salesByDayRes.data);
            setSalesByHour(salesByHourRes.data);
        } catch (error) {
            message.error("Failed to fetch report data");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <Spin spinning={loading}>
                <h2>Sales Report</h2>
                <DatePicker.RangePicker
                    onChange={(dates) => {
                        setStartDate(dates ? dates[0] : null);
                        setEndDate(dates ? dates[1] : null);
                    }}
                />
                <Button type="primary" onClick={handleGenerateReport} disabled={!startDate || !endDate}
                        style={{marginTop: 16}}>
                    Generate Report
                </Button>

                {/* Summary Section */}
                <Row gutter={16} style={{marginTop: '20px'}}>
                    <Col span={8}>
                        <Card>
                            <Statistic
                                title="Total Revenue"
                                value={totalRevenue}
                                prefix="$"
                                precision={2}
                                valueStyle={{color: '#3f8600'}}
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card>
                            <Statistic
                                title="Total Orders Count"
                                value={ordersCount}
                                valueStyle={{color: '#1890ff'}}
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card>
                            <Statistic
                                title="Average Order Value"
                                value={averageOrderValue}
                                prefix="$"
                                precision={2}
                                valueStyle={{color: '#cf1322'}}
                            />
                        </Card>
                    </Col>
                </Row>

                <div style={{display: 'flex', gap: '20px', marginTop: '20px'}}>
                    <div style={{flex: 1}}>
                        <h3>Revenue by Category</h3>
                        <CategoryRevenueChart data={categoryRevenue}/>
                    </div>
                    <div style={{flex: 1}}>
                        <h3>Quantity by Category</h3>
                        <CategoryQuantityChart data={categoryRevenue}/>
                    </div>
                </div>

                <div style={{display: 'flex', gap: '20px', marginTop: '20px'}}>
                    <div style={{flex: 1}}>
                        <CustomLineChart
                            data={salesByHour}
                            dataKey="totalRevenue"
                            xKey="hour"
                            yLabel="Revenue ($)"
                            title="Sales Revenue by Hour"
                            color="#2563eb"
                        />
                    </div>
                    <div style={{flex: 1}}>
                        <CustomLineChart
                            data={salesByHour}
                            dataKey="totalQuantity"
                            xKey="hour"
                            yLabel="Orders"
                            title="Sales Quantity by Hour"
                            color="#82ca9d"
                        />
                    </div>
                </div>

                <div style={{display: 'flex', gap: '20px', marginTop: '20px'}}>
                    <div style={{flex: 1}}>
                        <CustomLineChart
                            data={salesByDayOfWeek}
                            dataKey="totalRevenue"
                            xKey="dayOfWeek"
                            yLabel="Revenue ($)"
                            title="Sales Revenue by Day of Week"
                            color="#2563eb"
                        />
                    </div>
                    <div style={{flex: 1}}>
                        <CustomLineChart
                            data={salesByDayOfWeek}
                            dataKey="totalQuantity"
                            xKey="dayOfWeek"
                            yLabel="Orders"
                            title="Sales Quantity by Day of Week"
                            color="#82ca9d"
                        />
                    </div>
                </div>


                <div style={{marginTop: '20px'}}>
                    <h1>Sales Ranking</h1>
                    <TopSellingTable data={topSellingItems}/>
                </div>
            </Spin>
        </div>
    );
};

export default SalesReportPage;
