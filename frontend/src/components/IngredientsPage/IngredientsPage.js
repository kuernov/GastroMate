import React, { useEffect, useState } from "react";
import axios from "axios";
import {Button, Card, Input, message} from "antd";
import { useNavigate } from "react-router-dom";
import IngredientsForm from "./IngredientsForm";
import IngredientsTable from "./IngredientsTable";


const IngredientsPage = () => {
    const [ingredients, setIngredients] = useState([]);
    const [units, setUnits] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [searchQuery, setSearchQuery] = useState(""); // State for the search input
    const navigate = useNavigate();

    const fetchData = async (name = "") => {
        try {
            const token = localStorage.getItem("accessToken");
            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const [ingredientsResponse, unitsResponse] = await Promise.all([
                axios.get("http://localhost:8080/ingredients/grouped", {
                    headers: { Authorization: `Bearer ${token}` },
                    params: { name } // Pass search query as a parameter
                }),
                axios.get("http://localhost:8080/units", {
                    headers: { Authorization: `Bearer ${token}` },
                }),
            ]);
            setIngredients(ingredientsResponse.data);
            setUnits(unitsResponse.data);
            setLoading(false);
        } catch (error) {
            message.error("Failed to fetch data");
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, [navigate]);

    // Update ingredients when search query changes
    const handleSearch = (e) => {
        const value = e.target.value;
        setSearchQuery(value);
        fetchData(value);
    };

    // Funkcja do edytowania ilości
    const updateQuantity = async (ingredientId, newQuantity) => {
        try {
            const token = localStorage.getItem("accessToken");
            const response = await axios.put(
                `http://localhost:8080/ingredients/${ingredientId}/quantity`,
                null, // Body jest pusty, ponieważ dane są wysyłane w zapytaniu GET
                {
                    params: { newQuantity },
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            // Aktualizujemy stan lokalny
            setIngredients(ingredients.map(ing => ing.id === ingredientId ? response.data : ing));
            message.success("Quantity updated successfully!");
        } catch (error) {
            message.error("Failed to update quantity");
        }
    };

    // Funkcja do usuwania składnika
    const deleteIngredient = async (ingredientId) => {
        try {
            const token = localStorage.getItem("accessToken");
            await axios.delete(`http://localhost:8080/ingredients/${ingredientId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            // Usuwamy składnik z lokalnego stanu
            setIngredients(ingredients.filter(ing => ing.id !== ingredientId));
            message.success("Ingredient deleted successfully!");
        } catch (error) {
            message.error("Failed to delete ingredient");
        }
    };

    return (
        <div style={{ padding: "50px" }}>
            <Button type="primary" onClick={() => setShowForm(!showForm)}>
                {showForm ? "Hide Form" : "Add New Ingredient"}
            </Button>

            {showForm && (
                <IngredientsForm
                    units={units}
                    setIngredients={setIngredients}
                    ingredients={ingredients}
                    setUnits={setUnits}
                />
            )}

            <Card title="Your Ingredients" style={{ marginTop: "30px" }}>
                <Input
                    placeholder="Search by name"
                    value={searchQuery}
                    onChange={handleSearch}
                    style={{ marginBottom: 20 }}
                />
                <IngredientsTable
                    ingredients={ingredients}
                    units={units}
                    loading={loading}
                    onEditQuantity={updateQuantity}
                    onDelete={deleteIngredient}
                />
            </Card>
        </div>
    );
};

export default IngredientsPage;
