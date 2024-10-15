import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Card, message} from "antd";
import axios from "axios";
import MenuItemsForm from "./MenuItemsForm"
import MenuItemsTable from "./MenuItemsTable"
import menuItemsTable from "./MenuItemsTable";


const MenuItemsPage = () =>{
    const [menuItems, setMenuItems] = useState([]);
    const [categories, setCategories] = useState([]);
    const [units, setUnits] = useState([]);
    const [ingredients, setIngredients] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMenuItems = async () => {
            try {
                const token = localStorage.getItem("accessToken");
                if (!token) {
                    message.error("User not authenticated");
                    navigate("/login");
                }
                const [menuItemsResponse, categoriesResponse, unitsResponse, ingredientsResponse] = await Promise.all([
                    axios.get("http://localhost:8080/menu",{
                        headers: {Authorization: `Bearer ${token}`},
                    }),
                    axios.get("http://localhost:8080/categories",{
                        headers: {Authorization: `Bearer ${token}`},
                    }),
                    axios.get("http://localhost:8080/units",{
                        headers: {Authorization: `Bearer ${token}`},
                    }),
                    axios.get("http://localhost:8080/ingredients",{
                        headers: {Authorization: `Bearer ${token}`},
                    }),

                ]);
                setIngredients(ingredientsResponse.data);
                setUnits(unitsResponse.data);
                setCategories(categoriesResponse.data);
                setMenuItems(menuItemsResponse.data);
                setLoading(false);

            } catch(error){
                message.error("Failed to fetch menu items");
                setLoading(false);
            }
        };
        fetchMenuItems();
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

    return (
        <div style={{padding: "50px"}}>
            <Button type="primary" onClick={() => setShowForm(!showForm)}>
                {showForm ? "Hide Form" : "Add New Menu Item"}
            </Button>

            {showForm && (
                <MenuItemsForm
                    ingredients = {ingredients}
                    units = {units}
                    categories = {categories}
                    setCategories={setCategories}
                    menuItems = {menuItems}
                    setMenuItems = {setMenuItems}
                />
            )}

            <Card title="Your Menu Items" style={{marginTop: "30px"}}>
                <MenuItemsTable
                    menuItems = {menuItems}
                    categories={categories}
                    loading={loading}
                    units = {units}
                    onDelete={deleteMenuItem}
                />
            </Card>
        </div>
    );
};

export default MenuItemsPage