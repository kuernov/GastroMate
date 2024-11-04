import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Card, message, Checkbox } from "antd";
import axios from "axios";
import MenuItemsForm from "./MenuItemsForm";
import MenuItemsTable from "./MenuItemsTable";

const MenuItemsPage = () => {
    const [menuItems, setMenuItems] = useState([]);
    const [categories, setCategories] = useState([]);
    const [units, setUnits] = useState([]);
    const [ingredients, setIngredients] = useState([]);
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
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

                setIngredients(ingredientsResponse.data);
                setUnits(unitsResponse.data);
                setCategories(categoriesResponse.data);
                setMenuItems(menuItemsResponse.data);
                setLoading(false);
            } catch (error) {
                message.error("Failed to fetch menu items");
                setLoading(false);
            }
        };
        fetchData();
    }, [navigate]);

    const deleteMenuItem = async (menuItemId) => {
        try {
            const token = localStorage.getItem("accessToken");
            await axios.delete(`http://localhost:8080/menu/${menuItemId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            // Usuwamy składnik z lokalnego stanu
            setMenuItems(menuItems.filter(ing => ing.id !== menuItemId));
            message.success("Ingredient deleted successfully!");
        } catch (error) {
            message.error("Failed to delete ingredient");
        }
    };

    const handleFilterChange = (selectedIds) => {
        setSelectedIngredients(selectedIds);
    };

    const fetchFilteredMenuItems = async () => {
        try {
            const token = localStorage.getItem("accessToken");
            const response = await axios.post(
                "http://localhost:8080/menu/filter",
                {
                    ingredientIds: selectedIngredients
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            setMenuItems(response.data);
        } catch (error) {
            message.error("Failed to filter menu items");
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

            {/* Sekcja z filtrami */}
            <Card title="Filter by Ingredients" style={{ marginTop: "30px" }}>
                <Checkbox.Group
                    options={ingredients.map(ingredient => ({
                        label: ingredient.name,
                        value: ingredient.id,
                    }))}
                    onChange={handleFilterChange}
                />
                <Button onClick={fetchFilteredMenuItems} style={{ marginTop: "10px" }}>
                    Apply Filter
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
