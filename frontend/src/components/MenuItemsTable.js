import { Button, Form, Input, Popconfirm, Table } from "antd";
import React from "react";

const MenuItemsTable = ({ menuItems, categories, loading }) => {
    const [form] = Form.useForm();

    const getCategoryNamesByIds = (categoryIds) => {
        return categoryIds.map(categoryId => {
            const category = categories.find(c => c.id === categoryId);
            return category ? category.name : "Unknown Category";
        });
    };

    const getIngredientsDisplay = (menuItemIngredients) => {
        return menuItemIngredients.map(item => {
            return `${item.ingredientName}`;
        }).join(", ");
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
    ];

    return <Table dataSource={menuItems} columns={columns} rowKey="id" loading={loading} />;
};

export default MenuItemsTable;
