import axios from "axios";

const api = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});



// Request interceptor — add token header 
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);


// Response interceptor — unwrap ApiResponseDTO
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message || error.message || "Something went wrong";
    return Promise.reject(new Error(message));
  }
);

export default api;


api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  console.log("➡️ URL:", config.url);
  console.log("➡️ Token:", token);

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  console.log("➡️ Headers:", config.headers);

  return config;
});