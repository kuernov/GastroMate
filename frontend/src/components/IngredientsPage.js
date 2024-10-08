import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table, Card, message } from "antd";
import { useNavigate } from "react-router-dom";

const IngredientsPage = () => {
    const [ingredients, setIngredients] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchIngredients = async () => {
            try {
                const token = localStorage.getItem("accessToken"); // Zakładamy, że token jest przechowywany w localStorage po zalogowaniu
                if (!token) {
                    message.error("User not authenticated");
                    navigate("/login");
                    return;
                }

                // Żądanie do backendu, przekazując token w nagłówku autoryzacyjnym
                const response = await axios.get("http://localhost:8080/ingredients", {
                    headers: {
                        Authorization: `Bearer ${token}`, // Token JWT w nagłówku

                    },
                });

                setIngredients(response.data); // Zapisujemy odpowiedź z backendu w stanie
                setLoading(false); // Zakończono ładowanie
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    message.error("Unauthorized, please log in again.");
                    navigate("/login");
                } else {
                    message.error("Failed to fetch ingredients");
                }
                setLoading(false);
            }
        };

        fetchIngredients();
    }, [navigate]);

    const columns = [
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
        },
        {
            title: "Quantity",
            dataIndex: "quantity",
            key: "quantity",
        },
        {
            title: "Unit",
            dataIndex: "unit",
            key: "unit",
        },
    ];

    return (
        <div style={{ padding: "50px" }}>
            <Card title="Your Ingredients">
                <Table
                    dataSource={ingredients}
                    columns={columns}
                    rowKey="id"
                    loading={loading}
                />
            </Card>
        </div>
    );
};

export default IngredientsPage;
