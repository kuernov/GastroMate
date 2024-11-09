import React, { useState } from "react";
import { Table, Button, message, Form } from "antd";
import axios from "axios";
import '../styles/IngredientsTable.css';
import { useNavigate } from "react-router-dom";
import AdjustmentModal from "./AdjustmentModal";
import ExpandedTable from "./ExpandedTable";

const IngredientsTable = ({ ingredients, units, loading, onDelete }) => {
    const [editingId, setEditingId] = useState(null);
    const [expandedRowKeys, setExpandedRowKeys] = useState([]);
    const [editingUnitId, setEditingUnitId] = useState(null);
    const [adjustmentModalVisible, setAdjustmentModalVisible] = useState(false);
    const [form] = Form.useForm();
    const navigate = useNavigate();

    const getUnitNameById = (unitId) => {
        const unit = units.find(u => u.id === unitId);
        return unit ? unit.name : "Unknown Unit";
    };

    const handleAdjustmentClick = (record) => {
        setEditingId(record.id);
        setEditingUnitId(record.unitId);
        setAdjustmentModalVisible(true);
        form.resetFields();
    };

    const handleExpandRow = (productName) => {
        if (expandedRowKeys.includes(productName)) {
            setExpandedRowKeys(expandedRowKeys.filter(key => key !== productName));
        } else {
            setExpandedRowKeys([...expandedRowKeys, productName]);
        }
    };

    const onAdjustInventory = async (ingredientId, unitId, values) => {
        const { changeType } = values;
        const quantityChange = Number(values.quantity);
        try {
            const token = localStorage.getItem("accessToken");

            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            await axios.put(`http://localhost:8080/ingredients/${ingredientId}/quantity`, null, {
                params: { quantityChange, changeType },
                headers: { 'Authorization': `Bearer ${token}` }
            });

            message.success("Inventory adjusted successfully");
            setAdjustmentModalVisible(false);
            window.location.reload();
        } catch (error) {
            console.error("Error adjusting inventory:", error);
            message.error("Failed to adjust inventory");
        }
    };

    const handleAdjustmentSubmit = async () => {
        try {
            const values = await form.validateFields();
            await onAdjustInventory(editingId, editingUnitId, values);
        } catch (error) {
            console.error('Failed to adjust inventory:', error);
        }
    };

    const columns = [
        { title: "Name", dataIndex: "name", key: "name", width: "50%" },
        {
            title: 'Total Quantity (kg)',
            dataIndex: 'totalQuantity',
            key: "totalQuantity",
            render: (quantity) => {
                const numericQuantity = Number(quantity);
                return isNaN(numericQuantity) ? 'N/A' : `${numericQuantity.toFixed(2)} kg`;
            },
            width: "50%"
        },
    ];

    return (
        <>
            <Table
                columns={columns}
                expandable={{
                    expandedRowRender: (record) => (
                        <ExpandedTable
                            record={record}
                            onAdjustmentClick={handleAdjustmentClick}
                            onDelete={onDelete}
                            getUnitNameById={getUnitNameById}
                        />
                    ),
                    expandedRowKeys,
                    onExpand: (expanded, record) => handleExpandRow(record.name),
                    rowExpandable: (record) => record.ingredientDTOList && record.ingredientDTOList.length > 0,
                }}
                dataSource={ingredients}
                rowKey={(record) => record.name}
                loading={loading}
                rowClassName={(record) => expandedRowKeys.includes(record.name) ? 'expanded-row' : ''}
            />

            <AdjustmentModal
                visible={adjustmentModalVisible}
                onCancel={() => setAdjustmentModalVisible(false)}
                onSubmit={handleAdjustmentSubmit}
                form={form}
            />
        </>
    );
};

export default IngredientsTable;
