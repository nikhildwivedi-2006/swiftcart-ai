import { createContext, useContext, useState, useEffect } from "react";
// ❌ OLD - yeh hatao
// import { getAllUsers } from '../services/userService';

// ✅ NEW - yeh add karo
import userService from "../services/userService";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // ✅ Sirf tab fetch karo jab token ho
    const token = localStorage.getItem("token");
    if (token) {
      const user = JSON.parse(localStorage.getItem("user") || "null");
      setCurrentUser(user);
      fetchUsers(); // ✅ Token ke sath fetch karega
    } else {
      setLoading(false);
    }
  }, []);

  const fetchUsers = async () => {
    try {
      // ✅ userService.getAllUsers() use karo
      const data = await userService.getAllUsers();
      setUsers(data);
    } catch (error) {
      console.error("Failed to load users:", error);
      // ✅ 403 pe silent fail karo
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  const selectUser = (user) => {
    setCurrentUser(user);
    localStorage.setItem("user", JSON.stringify(user));
  };

  const refreshCurrentUser = async (id, userData) => {
    const updatedUser = await userService.updateUser(id, userData);

    setCurrentUser(updatedUser);
    localStorage.setItem("user", JSON.stringify(updatedUser));
  };

  return (
    <UserContext.Provider
      value={{
        users,
        currentUser,
        selectUser,
        loading,
        refreshCurrentUser,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);
