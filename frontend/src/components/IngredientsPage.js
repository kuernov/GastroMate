import React, { useEffect, useState } from "react";
import axios from "axios";
import { Button, Card, message } from "antd";
import { useNavigate } from "react-router-dom";
import IngredientsForm from "./IngredientsForm";
import IngredientsTable from "./IgredientsTable"


const IngredientsPage = () => {
    const [ingredients, setIngredients] = useState([]);
    const [units, setUnits] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
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

                const [ingredientsResponse, unitsResponse] = await Promise.all([
                    axios.get("http://localhost:8080/ingredients", {
                        headers: { Authorization: `Bearer ${token}` },
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

        fetchData();
    }, [navigate]);

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
                />
            )}

            <Card title="Your Ingredients" style={{ marginTop: "30px" }}>
                <IngredientsTable
                    ingredients={ingredients}
                    units={units}
                    loading={loading}
                    onEditQuantity={updateQuantity}  // Funkcja do edycji ilości
                    onDelete={deleteIngredient}  // Funkcja do usuwania składnika
                />
            </Card>
        </div>
    );
};

export default IngredientsPage;
