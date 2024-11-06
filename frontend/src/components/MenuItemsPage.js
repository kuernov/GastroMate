import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Card, message, Select, InputNumber } from "antd";
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
    const navigate = useNavigate();

    useEffect(() => {
        fetchMenuItems();
    }, [navigate]);

    const fetchMenuItems = async () => {
        try {
            const token = localStorage.getItem("accessToken");
            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const [menuItemsResponse, categoriesResponse, unitsResponse, ingredientsResponse] = await Promise.all([
                axios.get("http://localhost:8080/menu", {
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

            // Sortowanie składników i kategorii alfabetycznie
            const sortedIngredients = ingredientsResponse.data.sort((a, b) => a.name.localeCompare(b.name));
            const sortedCategories = categoriesResponse.data.sort((a, b) => a.name.localeCompare(b.name));

            setIngredients(sortedIngredients);
            setUnits(unitsResponse.data);
            setCategories(sortedCategories);
            setMenuItems(menuItemsResponse.data);
            setLoading(false);
        } catch (error) {
            message.error("Failed to fetch menu items");
            setLoading(false);
        }
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

    const fetchFilteredMenuItems = async () => {
        try {
            const token = localStorage.getItem("accessToken");

            const ingredientsParam = selectedIngredients.join(",");

            const response = await axios.get("http://localhost:8080/menu/filter", {
                params: {
                    size: selectedSize,
                    category: selectedCategory,
                    ingredients: ingredientsParam, // Przekazujemy składniki jako jeden ciąg
                    minPrice,
                    maxPrice,
                },
                headers: { Authorization: `Bearer ${token}` },
            });
            setMenuItems(response.data);
        } catch (error) {
            message.error("Failed to filter menu items");
        }
    };


    // Funkcja resetująca filtry
    const resetFilters = () => {
        setSelectedIngredients([]);
        setSelectedCategory(null);
        setSelectedSize(null);
        setMinPrice(null);
        setMaxPrice(null);
        fetchMenuItems(); // Wczytaj ponownie wszystkie elementy menu
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

            {/* Sekcja z filtrami */}
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
            </Card>
        </div>
    );
};

export default MenuItemsPage;
