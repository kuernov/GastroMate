import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Card, message} from "antd";
import axios from "axios";
import MenuItemsForm from "./MenuItemsForm"
import MenuItemsTable from "./MenuItemsTable"


const MenuItemsPage = () =>{
    const [menuItems, setMenuItems] = useState([]);
    const [categories, setCategories] = useState([]);
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
                const [menuItemsResponse, categoriesResponse] = await Promise.all([
                    axios.get("http://localhost:8080/menu",{
                        headers: {Authorization: `Bearer ${token}`},
                    }),
                    axios.get("http://localhost:8080/categories",{
                        headers: {Authorization: `Bearer ${token}`},
                    }),

                ]);
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

    return (
        <div style={{padding: "50px"}}>
            <Button type="primary" onClick={() => setShowForm(!showForm)}>
                {showForm ? "Hide Form" : "Add New Menu Item"}
            </Button>

            {showForm && (
                <MenuItemsForm
                    menuItems = {menuItems}
                    setMenuItems = {setMenuItems}
                />
            )}

            <Card title="Your Menu Items" style={{marginTop: "30px"}}>
                <MenuItemsTable
                    menuItems = {menuItems}
                    categories={categories}
                    loading={loading}

                />
            </Card>
        </div>
    );
};

export default MenuItemsPage