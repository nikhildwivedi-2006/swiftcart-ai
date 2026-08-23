import api from "./api";

export const loginUser = async (email, password) => {
    const response = await api.post("/auth/login", { email, password });
    return response.data.data;
};

export const registerUser = async (name, email, password, contactNumber) => {
    const response = await api.post("/auth/register", {
        name,
        email,
        password,
        contactNumber,
    });
    return response.data;
};

export const logoutUser = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
};

export const getToken = () => localStorage.getItem("token");

export const isLoggedIn = () => !!getToken();