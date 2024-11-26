import axios from "axios";
import { TokenService } from "./components/services/TokenService";
import { refreshAccessToken } from "./components/services/AuthService";

const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL || "http://localhost:8080",
    withCredentials: true,
});

// Dodanie tokena do nagłówków każdego żądania
api.interceptors.request.use(
    (config) => {
        const token = TokenService.getToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Obsługa błędów odpowiedzi
api.interceptors.response.use(
    (response) => response, // Jeśli odpowiedź jest poprawna, zwróć ją
    async (error) => {
        const originalRequest = error.config;

        // Sprawdzenie, czy odpowiedź to błąd 401 (Unauthorized)
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true; // Zapobiega wielokrotnemu odświeżaniu tego samego żądania
            try {
                const newAccessToken = await refreshAccessToken();
                TokenService.setToken(newAccessToken);
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return api(originalRequest);
            } catch (refreshError) {
                TokenService.clearToken();
                window.location.href = "/login";
                return Promise.reject(refreshError);
            }
        }

        // Jeśli to nie jest błąd 401, przekaż dalej
        return Promise.reject(error);
    }
);

export default api;
