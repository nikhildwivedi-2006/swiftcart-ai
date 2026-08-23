import api from "./api"; // tumhara axios instance

export const askAI = async (message) => {
    const response = await api.post("/ai/chat", {
        message: message
    });

    return response.data;
};