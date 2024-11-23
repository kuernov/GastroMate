import React, { useEffect, useState } from "react";
import { Button, Card, Input, message } from "antd";
import { useNavigate } from "react-router-dom";
import IngredientsForm from "./IngredientsForm";
import IngredientsTable from "./IngredientsTable";
import api from "../../api"; // Import globalnej instancji axios

const IngredientsPage = () => {
    const [ingredients, setIngredients] = useState([]);
    const [units, setUnits] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showForm, setShowForm] = useState(false);
    const [searchQuery, setSearchQuery] = useState(""); // State for the search input
    const navigate = useNavigate();

    const fetchData = async (name = "") => {
        setLoading(true);
        try {
            const [ingredientsResponse, unitsResponse] = await Promise.all([
                api.get("/ingredients/grouped", { params: { name } }),
                api.get("/units"),
            ]);

            setIngredients(ingredientsResponse.data);
            setUnits(unitsResponse.data);
        } catch (error) {
            if (error.response?.status === 401) {
                message.error("Session expired. Please log in again.");
                navigate("/login");
            } else {
                message.error("Failed to fetch data.");
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleSearch = (e) => {
        const value = e.target.value;
        setSearchQuery(value);
        fetchData(value);
    };

    const updateQuantity = async (ingredientId, newQuantity) => {
        try {
            const response = await api.put(`/ingredients/${ingredientId}/quantity`, {
                newQuantity,
            });
            setIngredients((prev) =>
                prev.map((ing) => (ing.id === ingredientId ? response.data : ing))
            );
            message.success("Quantity updated successfully!");
        } catch (error) {
            message.error("Failed to update quantity.");
        }
    };

    const deleteIngredient = async (ingredientId) => {
        try {
            await api.delete(`/ingredients/${ingredientId}`);
            setIngredients((prev) => prev.filter((ing) => ing.id !== ingredientId));
            message.success("Ingredient deleted successfully!");
        } catch (error) {
            message.error("Failed to delete ingredient.");
        }
    };

    return (
        <div style={{ padding: "50px" }}>
            <Button type="primary" onClick={() => setShowForm((prev) => !prev)}>
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
