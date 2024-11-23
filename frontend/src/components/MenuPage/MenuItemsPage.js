import React, { useState, useEffect } from "react";
import { Button, Card, message } from "antd";
import { useNavigate } from "react-router-dom";
import "../styles/MenuItemsPage.css";
import MenuItemsTable from "./MenuItemsTable";
import MenuItemsFilter from "./MenuItemsFilter";
import MenuItemsPagination from "./MenuItemsPagination";
import MenuItemsForm from "./MenuItemsForm";
import useMenuItems from "./useMenuItems";

const MenuItemsPage = () => {
    const [showForm, setShowForm] = useState(false);
    const navigate = useNavigate();

    const { menuItems, categories, units, ingredients, totalItems, loading, fetchMenuItems, deleteMenuItem, filters, setFilters, page, setPage, pageSize, setPageSize } = useMenuItems();

    return (
        <div style={{ padding: "50px" }}>
            <Button type="primary" onClick={() => setShowForm(!showForm)}>
                {showForm ? "Hide Form" : "Add New Menu Item"}
            </Button>

            {showForm && (
                <MenuItemsForm
                    ingredients={ingredients}
                    units={units}
                    categories={categories}
                />
            )}

            <Card title="Filter" style={{ marginTop: "30px" }}>
                <MenuItemsFilter
                    ingredients={ingredients}
                    categories={categories}
                    filters={filters}
                    setFilters={setFilters}
                    fetchFilteredMenuItems={() => {
                        setPage(1);
                        fetchMenuItems(1, pageSize);
                    }}
                    resetFilters={() => {
                        setFilters({});
                        setPage(1);
                        fetchMenuItems(1, pageSize);
                    }}
                />
            </Card>

            <Card title="Your Menu Items" style={{ marginTop: "30px" }}>
                <MenuItemsTable
                    menuItems={menuItems}
                    categories={categories}
                    loading={loading}
                    units={units}
                    onDelete={deleteMenuItem}
                />
                <MenuItemsPagination
                    page={page}
                    pageSize={pageSize}
                    totalItems={totalItems}
                    onPageChange={(newPage) => setPage(newPage)}
                />
            </Card>
        </div>
    );
};

export default MenuItemsPage;
