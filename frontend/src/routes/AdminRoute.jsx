import { Navigate } from "react-router-dom";
import { useUser } from "../context/UserContext";

export default function AdminRoute({ children }) {
  const { currentUser, loading } = useUser();

  if (loading) {
    return <p>Loading...</p>;
  }

  if (!currentUser) {
    return <Navigate to="/login" replace />;
  }

  if (currentUser.role !== "ADMIN") {
    return <Navigate to="/" replace />;
  }

  return children;
}
