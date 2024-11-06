import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Card, message, Select, InputNumber, Pagination } from "antd";
import axios from "axios";
import MenuItemsForm from "./MenuItemsForm";
import MenuItemsTable from "./MenuItemsTable";
import "./styles/MenuItemsPage.css";

const { Option } = Select;

const MenuItemsPage = () => {
    const [menuItems, setMenuItems] = useState([]);
    const [categories, setCategories] = useState([]);
    const [units, setUnits] = useState([]);
    const [ingredients, setIngredients] = useState([]);
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [selectedSize, setSelectedSize] = useState(null);
    const [minPrice, setMinPrice] = useState(null);
    const [maxPrice, setMaxPrice] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);           // Current page
    const [pageSize, setPageSize] = useState(10);   // Page size
    const [totalItems, setTotalItems] = useState(0); // Total items count
    const navigate = useNavigate();

    const filters = {
        selectedSize,
        selectedCategory,
        selectedIngredients,
        minPrice,
        maxPrice,
    };

    useEffect(() => {
        fetchMenuItems(page, pageSize, filters);
    }, [navigate, page, pageSize]);

    const fetchMenuItems = async (page, pageSize, filters) => {
        try {
            const token = localStorage.getItem("accessToken");
            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const ingredientsParam = filters.selectedIngredients ? filters.selectedIngredients.join(",") : undefined;
            const params = {
                page: page - 1,
                pageSize,
                size: filters.selectedSize || undefined,
                category: filters.selectedCategory || undefined,
                ingredients: ingredientsParam,
                minPrice: filters.minPrice || undefined,
                maxPrice: filters.maxPrice || undefined,
            };

            const [menuItemsResponse, categoriesResponse, unitsResponse, ingredientsResponse] = await Promise.all([
                axios.get("http://localhost:8080/menu/filter", {
                    params: params,
                    headers: { Authorization: `Bearer ${token}` },
                }),
                axios.get("http://localhost:8080/categories", {
                    headers: { Authorization: `Bearer ${token}` },
                }),
                axios.get("http://localhost:8080/units", {
                    headers: { Authorization: `Bearer ${token}` },
                }),
                axios.get("http://localhost:8080/ingredients", {
                    headers: { Authorization: `Bearer ${token}` },
                }),
            ]);

            const sortedIngredients = ingredientsResponse.data.sort((a, b) => a.name.localeCompare(b.name));
            const sortedCategories = categoriesResponse.data.sort((a, b) => a.name.localeCompare(b.name));

            setIngredients(sortedIngredients);
            setUnits(unitsResponse.data);
            setCategories(sortedCategories);
            setMenuItems(menuItemsResponse.data.content);
            setTotalItems(menuItemsResponse.data.totalElements);
            console.log(totalItems);
            setLoading(false);
        } catch (error) {
            message.error("Failed to fetch menu items");
            setLoading(false);
        }
    };

    const fetchFilteredMenuItems = () => {
        setPage(1);  // Reset to first page for new filters
        fetchMenuItems(1, pageSize, filters);
    };

    const resetFilters = () => {
        setSelectedIngredients([]);
        setSelectedCategory(null);
        setSelectedSize(null);
        setMinPrice(null);
        setMaxPrice(null);
        setPage(1);
        fetchMenuItems(1, pageSize, {});
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    const deleteMenuItem = async (menuItemId) => {
        try {
            const token = localStorage.getItem("accessToken");
            await axios.delete(`http://localhost:8080/menu/${menuItemId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            setMenuItems(menuItems.filter((item) => item.id !== menuItemId));
            message.success("Ingredient deleted successfully!");
        } catch (error) {
            message.error("Failed to delete ingredient");
        }
    };

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
                    setCategories={setCategories}
                    menuItems={menuItems}
                    setMenuItems={setMenuItems}
                />
            )}

            <Card title="Filter" style={{ marginTop: "30px" }}>
                <div style={{ marginTop: "10px" }}>
                    <Select
                        mode="multiple"
                        placeholder="Select Ingredients"
                        onChange={(value) => setSelectedIngredients(value)}
                        style={{ width: "300px", marginBottom: "10px" }}
                        value={selectedIngredients}
                    >
                        {ingredients.map((ingredient) => (
                            <Option key={ingredient.id} value={ingredient.name}>
                                {ingredient.name}
                            </Option>
                        ))}
                    </Select>

                    <Select
                        placeholder="Select Category"
                        onChange={(value) => setSelectedCategory(value)}
                        style={{ width: "200px", marginRight: "10px" }}
                        value={selectedCategory}
                    >
                        {categories.map((category) => (
                            <Option key={category.id} value={category.name}>
                                {category.name}
                            </Option>
                        ))}
                    </Select>

                    <Select
                        placeholder="Select Size"
                        onChange={(value) => setSelectedSize(value)}
                        style={{ width: "200px", marginRight: "10px" }}
                        value={selectedSize}
                    >
                        <Option value="S">Small</Option>
                        <Option value="M">Medium</Option>
                        <Option value="L">Large</Option>
                    </Select>

                    <InputNumber
                        placeholder="Min Price"
                        min={0}
                        onChange={(value) => setMinPrice(value)}
                        style={{ marginRight: "10px" }}
                        value={minPrice}
                    />
                    <InputNumber
                        placeholder="Max Price"
                        min={0}
                        onChange={(value) => setMaxPrice(value)}
                        value={maxPrice}
                    />
                </div>

                <Button onClick={fetchFilteredMenuItems} style={{ marginTop: "10px", marginRight: "10px" }}>
                    Apply Filter
                </Button>
                <Button onClick={resetFilters} style={{ marginTop: "10px" }}>
                    Clear Filters
                </Button>
            </Card>

            <Card title="Your Menu Items" style={{ marginTop: "30px" }}>
                <MenuItemsTable
                    menuItems={menuItems}
                    categories={categories}
                    loading={loading}
                    units={units}
                    onDelete={deleteMenuItem}
                />
                <Pagination
                    current={page}
                    pageSize={pageSize}
                    total={totalItems}
                    onChange={handlePageChange}
                    style={{ marginTop: "20px", textAlign: "center" }}
                />
            </Card>
        </div>
    );
};

export default MenuItemsPage;
