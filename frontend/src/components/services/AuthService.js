import { TokenService } from "./TokenService";
import axios from "axios";

export const login = async (email, password) => {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, password }),
            credentials: "include", // Włączenie ciasteczek HttpOnly
        });

        if (!response.ok) {
            throw new Error("Login failed");
        }

        const data = await response.json();
        TokenService.setToken(data.accessToken); // Zapisanie access token
        return data;
    } catch (error) {
        console.error("Error during login:", error);
        throw error;
    }
};

export const register = async (userData) => {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        });

        if (!response.ok) {
            throw new Error("Registration failed");
        }

        return await response.json();
    } catch (error) {
        console.error("Error during registration:", error);
        throw error;
    }
};

export const refreshAccessToken = async () => {
    try {
        const response = await axios.post(
            `${process.env.REACT_APP_API_URL || "http://localhost:8080"}/refresh-token`,
            {},
            { withCredentials: true } // Włączenie ciasteczek HttpOnly
        );

        if (response.status === 200) {
            return response.data.accessToken; // Zwraca nowy access token
        } else {
            throw new Error("Failed to refresh token");
        }
    } catch (error) {
        console.error("Error refreshing access token:", error);
        throw error;
    }
};

export const logout = async () => {
    try {
        await fetch(`${process.env.REACT_APP_API_URL}/logout`, {
            method: "POST",
            credentials: "include", // Obsługa ciasteczek HttpOnly
        });

        TokenService.clearToken(); // Usunięcie access token
        return true;
    } catch (error) {
        console.error("Error during logout:", error);
        throw error;
    }
};
