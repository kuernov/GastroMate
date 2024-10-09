import React, { useState } from "react";
import {Table, Button, InputNumber, Input, Popconfirm, Form} from "antd";

const IngredientsTable = ({ ingredients, units, loading, onEditQuantity, onDelete }) => {
    const [editingId, setEditingId] = useState(null);  // Track the ingredient being edited
    const [form] = Form.useForm();
    const getUnitNameById = (unitId) => {
        const unit = units.find(u => u.id === unitId);
        return unit ? unit.name : "Unknown Unit";
    };
    const isEditing = (record) => record.id === editingId;

    const handleEditClick = (record) => {
        setEditingId(record.id);
        form.setFieldsValue({ quantity: record.quantity });
    };

    const handleSave = async (id) => {
        try {
            const values = await form.validateFields();
            onEditQuantity(id, values.quantity);
            setEditingId(null);  // Reset the editing state after saving
        } catch (error) {
            console.error('Failed to save:', error);
        }
    };

    const handleCancel = () => {
        setEditingId(null);
    };
    const columns = [
        {
            title: "ID",
            dataIndex: "id",
            key: "id",
        },
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
        },
        {
            title: 'Quantity',
            dataIndex: 'quantity',
            key: 'quantity',
            render: (_, record) => {
                if (isEditing(record)) {
                    return (
                        <Form form={form}>
                            <Form.Item
                                name="quantity"
                                style={{ margin: 0 }}
                                rules={[{ required: true, message: 'Please input the quantity' }]}
                            >
                                <Input type="number" style={{ width: '100px' }} />
                            </Form.Item>
                        </Form>
                    );
                }
                return <span>{record.quantity}</span>;
            },
        },
        {
            title: "Unit",
            dataIndex: "unit",
            key: "unit",
            render: (text, record) => getUnitNameById(record.unitId),
        },
        {
            title: "Expiry Date",
            dataIndex: "expiryDate",
            key: "expiryDate",
            render: (date) => date ? new Date(date).toLocaleDateString() : 'N/A',
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_, record) => {
                const editable = isEditing(record);
                return editable ? (
                    <span>
            <Button type="link" onClick={() => handleSave(record.id)}>Save</Button>
            <Button type="link" onClick={handleCancel}>Cancel</Button>
          </span>
                ) : (
                    <>
                        <Button type="link" onClick={() => handleEditClick(record)}>Edit</Button>
                        <Popconfirm
                            title="Are you sure to delete this ingredient?"
                            onConfirm={() => onDelete(record.id)}
                        >
                            <Button type="link" danger>Delete</Button>
                        </Popconfirm>
                    </>
                );
            },
        },
    ];

    return <Table dataSource={ingredients} columns={columns} rowKey="id" loading={loading} />;
};

export default IngredientsTable;
