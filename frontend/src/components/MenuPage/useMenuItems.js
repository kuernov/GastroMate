import { useState, useEffect } from "react";
import axios from "axios";
import { message } from "antd";
import { useNavigate } from "react-router-dom";
import api from "../../api";

const useMenuItems = () => {
    const [menuItems, setMenuItems] = useState([]);
    const [categories, setCategories] = useState([]);
    const [units, setUnits] = useState([]);
    const [ingredients, setIngredients] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({});
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const navigate = useNavigate();

    const fetchMenuItems = async (currentPage, currentPageSize) => {
        try {
            setLoading(true);

            const ingredientsParam = filters.selectedIngredients
                ? filters.selectedIngredients.join(",")
                : undefined;

            const params = {
                page: currentPage - 1,
                pageSize: currentPageSize,
                name: filters.name || undefined,
                size: filters.selectedSize || undefined,
                category: filters.selectedCategory || undefined,
                ingredients: ingredientsParam,
                minPrice: filters.minPrice || undefined,
                maxPrice: filters.maxPrice || undefined,
            };

            const [menuItemsResponse, categoriesResponse, unitsResponse, ingredientsResponse] = await Promise.all([
                api.get("/menu/filter", { params }),
                api.get("/categories"),
                api.get("/units"),
                api.get("/ingredients"),
            ]);

            const sortedIngredients = ingredientsResponse.data.sort((a, b) =>
                a.name.localeCompare(b.name)
            );
            const sortedCategories = categoriesResponse.data.sort((a, b) =>
                a.name.localeCompare(b.name)
            );

            setIngredients(sortedIngredients);
            setUnits(unitsResponse.data);
            setCategories(sortedCategories);
            setMenuItems(menuItemsResponse.data.content);
            setTotalItems(menuItemsResponse.data.totalElements);
        } catch (error) {
            message.error("Failed to fetch menu items");
        } finally {
            setLoading(false);
        }
    };

    const deleteMenuItem = async (menuItemId) => {
        try {
            await api.delete(`/menu/${menuItemId}`);
            setMenuItems((prevMenuItems) =>
                prevMenuItems.filter((item) => item.id !== menuItemId)
            );
            message.success("Menu item deleted successfully!");
        } catch (error) {
            message.error("Failed to delete menu item");
        }
    };


    useEffect(() => {
        fetchMenuItems(page, pageSize);
    }, [page, pageSize, filters]);

    return {
        menuItems,
        categories,
        units,
        ingredients,
        totalItems,
        loading,
        fetchMenuItems,
        deleteMenuItem,
        filters,
        setFilters,
        page,
        setPage,
        pageSize,
        setPageSize,
    };
};

export default useMenuItems;
