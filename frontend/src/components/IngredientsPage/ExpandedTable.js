import React from "react";
import { Table, Button, Popconfirm } from "antd";

const ExpandedTable = ({ record, onAdjustmentClick, onDelete, getUnitNameById }) => {
    const detailColumns = [
        { title: "ID", dataIndex: "id", key: "id", width: "10%" },
        { title: "Name", dataIndex: "name", key: "name", width: "15%" },
        {
            title: "Expiry Date",
            dataIndex: "expiryDate",
            key: "expiryDate",
            render: (date) => date ? new Date(date).toLocaleDateString() : 'N/A',
            width: "15%"
        },
        {
            title: "Quantity",
            dataIndex: "quantity",
            key: "quantity",
            render: (quantity) => quantity ? quantity.toFixed(2) : "0.00",
            width: "10%"
        },
        {
            title: "Unit",
            dataIndex: "unitId",
            key: "unitId",
            render: (unitId) => getUnitNameById(unitId),
            width: "20%"
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_, detailRecord) => (
                <>
                    <Button type="link" onClick={() => onAdjustmentClick(detailRecord)}>Adjust Inventory</Button>
                    <Popconfirm
                        title="Are you sure to delete this ingredient?"
                        onConfirm={() => onDelete(detailRecord.id)}
                    >
                        <Button type="link" danger>Delete</Button>
                    </Popconfirm>
                </>
            ),
            width: "10%",
        },
    ];

    return (
        <Table
            columns={detailColumns}
            dataSource={record.ingredientDTOList}
            pagination={false}
            rowKey={(detail) => detail.id}
        />
    );
};

export default ExpandedTable;
