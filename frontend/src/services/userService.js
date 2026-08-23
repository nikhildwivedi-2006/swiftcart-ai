import api from "./api";

const userService = {
  // POST /api/users
  createUser: async (userData) => {
    const response = await api.post("/users", userData);
    return response.data.data;
  },

  // GET /api/users/{id}
  getUserById: async (id) => {
    const response = await api.get(`/users/${id}`);
    return response.data.data;
  },

   // GET /api/users
  getAllUsers: async () => {
    try {
      const response = await api.get("/users");
      return response.data.data || [];
    } catch (error) {
      // ✅ 403 error handle karo
      if (error.response?.status === 403) {
        console.warn('⚠️ User fetch not allowed - returning empty array');
        return [];
      }
      throw error;
    }
  },

  // GET /api/users/email/{email}
  getUserByEmail: async (email) => {
    const response = await api.get(`/users/email/${email}`);
    return response.data.data;
  },

  // GET /api/users/active
  getActiveUsers: async () => {
    const response = await api.get("/users/active");
    return response.data.data;
  },

  // PUT /api/users/{id}
  updateUser: async (id, userData) => {
    const response = await api.put(`/users/${id}`, userData);
    return response.data.data;
  },

  // PATCH /api/users/{id}/activate
  activateUser: async (id) => {
    const response = await api.patch(`/users/${id}/activate`);
    return response.data;
  },

  // PATCH /api/users/{id}/deactivate
  deactivateUser: async (id) => {
    const response = await api.patch(`/users/${id}/deactivate`);
    return response.data;
  },

  // GET /api/users/search?keyword=xyz
  searchUsers: async (keyword) => {
    const response = await api.get("/users/search", { params: { keyword } });
    return response.data.data;
  },

  // GET /api/users/check-email?email=xyz
  checkEmailExists: async (email) => {
    const response = await api.get("/users/check-email", {
      params: { email },
    });
    return response.data.data;
  },
};

export default userService;
