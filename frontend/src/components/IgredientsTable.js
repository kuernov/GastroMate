import React, { useState } from "react";
import { Table, Button, Popconfirm, Form, Input } from "antd";
import './styles/IngredientsTable.css'; // Add your custom CSS file

const IngredientsTable = ({ ingredients, units, loading, onEditQuantity, onDelete }) => {
    const [editingId, setEditingId] = useState(null);
    const [expandedRowKeys, setExpandedRowKeys] = useState([]);
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
            await onEditQuantity(id, values.quantity);
            setEditingId(null);
            window.location.reload()
        } catch (error) {
            console.error('Failed to save:', error);
        }
    };

    const handleCancel = () => {
        setEditingId(null);
    };

    const columns = [
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
            width: "50%",

        },
        {
            title: 'Total Quantity (kg)',
            dataIndex: 'totalQuantity',
            key: "totalQuantity",
            render: (quantity) => `${quantity.toFixed(2)} kg`,
            width: "50%",
        },

    ];

    const expandedRowRender = (record) => {
        const detailColumns = [
            {
                title: "ID",
                dataIndex: "id",
                key: "id",
                width: "10%",
            },
            {
                title: "Name",
                dataIndex: "name",
                key: "name",
                width: "15%",
            },
            {
                title: "Expiry Date",
                dataIndex: "expiryDate",
                key: "expiryDate",
                render: (date) => date ? new Date(date).toLocaleDateString() : 'N/A',
                width: "15%",
            },
            {
                title: "Quantity",
                dataIndex: "quantity",
                key: "quantity",
                render: (_, detailRecord) => {
                    if (isEditing(detailRecord)) {
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
                    return <span>{detailRecord.quantity}</span>;
                },
                width: "10%",
            },
            {
                title: "Unit",
                dataIndex: "unitId",
                key: "unitId",
                render: (unitId) => getUnitNameById(unitId),
                width: "20%",
            },
            {
                title: 'Actions',
                key: 'actions',
                render: (_, detailRecord) => {
                    const editable = isEditing(detailRecord);
                    return editable ? (
                        <span>
                            <Button type="link" onClick={() => handleSave(detailRecord.id)}>Save</Button>
                            <Button type="link" onClick={handleCancel}>Cancel</Button>
                        </span>
                    ) : (
                        <>
                            <Button type="link" onClick={() => handleEditClick(detailRecord)}>Edit</Button>
                            <Popconfirm
                                title="Are you sure to delete this ingredient?"
                                onConfirm={() => onDelete(detailRecord.id)}
                            >
                                <Button type="link" danger>Delete</Button>
                            </Popconfirm>
                        </>
                    );
                },
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

    const handleExpandRow = (productName) => {
        if (expandedRowKeys.includes(productName)) {
            setExpandedRowKeys(expandedRowKeys.filter(key => key !== productName));
        } else {
            setExpandedRowKeys([...expandedRowKeys, productName]);
        }
    };

    return (
        <Table
            columns={columns}
            expandable={{
                expandedRowRender,
                expandedRowKeys,
                onExpand: (expanded, record) => {
                    handleExpandRow(record.name);
                },
                rowExpandable: (record) => record.ingredientDTOList && record.ingredientDTOList.length > 0,
            }}
            dataSource={ingredients}
            rowKey={(record) => record.name}
            loading={loading}
            rowClassName={(record) => expandedRowKeys.includes(record.name) ? 'expanded-row' : ''}
        />
    );
};

export default IngredientsTable;
