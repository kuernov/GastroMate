import { Button, Form, Input, Popconfirm, Table } from "antd";
import './styles/MenuItemsTable.css';
import React, {useState} from "react";

const MenuItemsTable = ({ menuItems, categories, loading, units, onDelete }) => {
    const [form] = Form.useForm();
    const [expandedRowKeys, setExpandedRowKeys] = useState([]);


    const getCategoryNamesByIds = (categoryIds) => {
        return categoryIds.map(categoryId => {
            const category = categories.find(c => c.id === categoryId);
            return category ? category.name : "Unknown Category";
        });
    };

    const getUnitNameById = (unitId) => {
        const unit = units.find(u => u.id === unitId);
        return unit ? unit.name : "Unknown Unit";
    };

    const getIngredientsDisplay = (menuItemIngredients) => {
        return menuItemIngredients.map(item => {
            return `${item.ingredientName}`;
        }).join(", ");
    };

    const expandedRowRender = (record) => {
        return (
            <Table
                columns={[
                    { title: 'Ingredient', dataIndex: 'ingredientName', key: 'ingredientName' },
                    { title: 'Quantity', dataIndex: 'quantityRequired', key: 'quantityRequired' },
                    {
                        title: 'Unit',
                        dataIndex: 'unitId',
                        key: 'unitId',
                        render: (text, record) => getUnitNameById(record.unitId)
                    }
                ]}
                dataSource={record.ingredients}
                pagination={false}
                rowKey="ingredientId"
            />
        );
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
            title: "Description",
            dataIndex: "description",
            key: "description",
        },
        {
            title: "Categories",
            dataIndex: "categories",
            key: "categories",
            render: (text, record) => getCategoryNamesByIds(record.categoryIds).join(", "), // Poprawione wywołanie .join()
        },
        {
            title: "Ingredients",
            dataIndex: "ingredients",
            key: "ingredients",
            render: (text, record) => getIngredientsDisplay(record.ingredients),
        },
        {
            title: "Price",
            dataIndex: "price",
            key: "price",
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (text, record) => (
                <>
                    <Popconfirm
                        title="Are you sure to delete this ingredient?"
                        onConfirm={() => onDelete(record.id)}
                    >
                        <Button type="link" danger>Delete</Button>
                    </Popconfirm>
                </>
            ),
        }
    ];

    const handleExpandRow = (id) => {
        if (expandedRowKeys.includes(id)) {
            // Jeśli wiersz jest rozwinięty, zamknij go
            setExpandedRowKeys(expandedRowKeys.filter(key => key !== id));
        } else {
            // Jeśli wiersz nie jest rozwinięty, otwórz go
            setExpandedRowKeys([...expandedRowKeys, id]);
        }
    };

    return (
        <Table
            dataSource={menuItems}
            columns={columns}
            rowKey="id"
            loading={loading}
            expandable={{
                expandedRowRender,
                expandedRowKeys,
                onExpand: (expanded, record) => {
                    handleExpandRow(record.id);
                },
                rowExpandable: (record) => record.ingredients.length > 0,
            }}
            rowClassName={(record) => expandedRowKeys.includes(record.id) ? 'expanded-row' : ''} // Dodajemy klasę do rozwiniętego wiersza
        />
    );
};


export default MenuItemsTable;
