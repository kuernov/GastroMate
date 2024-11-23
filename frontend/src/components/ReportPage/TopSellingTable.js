import React from 'react';
import { Table, Progress } from 'antd';

const TopSellingTable = ({ data }) => {
    const columns = [
        {
            title: 'Item Name',
            dataIndex: 'itemName',
            key: 'itemName',
            sorter: (a, b) => a.itemName.localeCompare(b.itemName),
        },
        {
            title: 'Quantity Sold',
            dataIndex: 'quantitySold',
            key: 'quantitySold',
            sorter: (a, b) => a.quantitySold - b.quantitySold,
        },
        {
            title: 'Total Revenue',
            dataIndex: 'totalRevenue',
            key: 'totalRevenue',
            render: (text) => `$${text.toFixed(2)}`,
            sorter: (a, b) => a.totalRevenue - b.totalRevenue,
        },
        {
            title: 'Quantity Share (%)',
            dataIndex: 'quantityPercentage',
            key: 'quantityPercentage',
            render: (value) => (
                <Progress
                    percent={value.toFixed(2)}
                    size="small"
                    status="active"
                    strokeColor={value > 1 ? '#52c41a' : '#ff4d4f'}  // Green if above 50%, red otherwise
                />
            ),
            sorter: (a, b) => a.quantityPercentage - b.quantityPercentage,
        },
        {
            title: 'Revenue Share (%)',
            dataIndex: 'revenuePercentage',
            key: 'revenuePercentage',
            render: (value) => (
                <Progress
                    percent={value.toFixed(2)}
                    size="small"
                    status="active"
                />
            ),
            sorter: (a, b) => a.revenuePercentage - b.revenuePercentage,
        },
    ];

    return <Table columns={columns} dataSource={data} rowKey="itemName" />;
};

export default TopSellingTable;
