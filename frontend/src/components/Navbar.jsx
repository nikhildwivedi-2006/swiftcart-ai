import { Link, useLocation } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useUser } from "../context/UserContext";

import {
  FiShoppingCart,
  FiPackage,
  FiGrid,
  FiHome,
  FiMessageCircle,
  FiLogOut,
  FiSettings,
} from "react-icons/fi";

export default function Navbar() {
  const { pathname } = useLocation();

  const { itemCount } = useCart();
  const { currentUser } = useUser();

  const user = currentUser || {};
  const userName = user?.email?.split("@")[0] || "User";

  return (
    <nav className="navbar">
      <div className="container">
        <Link to="/" className="navbar-brand">
          <FiShoppingCart />
          SwiftCart
        </Link>

        <div className="navbar-links">
          {/* Home */}
          <Link
            to="/"
            className={`nav-link ${pathname === "/" ? "active" : ""}`}
          >
            <FiHome size={18} />
            <span>Home</span>
          </Link>

          {/* Products */}
          <Link
            to="/products"
            className={`nav-link ${
              pathname.startsWith("/products") ? "active" : ""
            }`}
          >
            <FiGrid size={18} />
            <span>Products</span>
          </Link>

          {/* Cart */}
          <Link
            to="/cart"
            className={`nav-link nav-cart-badge ${
              pathname === "/cart" ? "active" : ""
            }`}
          >
            <FiShoppingCart size={18} />
            <span>Cart</span>

            {itemCount > 0 && (
              <span className="nav-cart-count">{itemCount}</span>
            )}
          </Link>

          {/* Orders */}
          <Link
            to="/orders"
            className={`nav-link ${
              pathname.startsWith("/orders") ? "active" : ""
            }`}
          >
            <FiPackage size={18} />
            <span>Orders</span>
          </Link>

          {/* Admin */}
          {user.role === "ADMIN" && (
            <Link
              to="/admin/products"
              className={`nav-link ${
                pathname.startsWith("/admin") ? "active" : ""
              }`}
            >
              <FiSettings size={18} />
              <span>Admin</span>
            </Link>
          )}

          {/* Support */}
          <Link
            to="/support"
            className={`nav-link ${
              pathname.startsWith("/support") ? "active" : ""
            }`}
          >
            <FiMessageCircle size={18} />
            <span>Support</span>
          </Link>

          {/* Profile */}
          <div className="profile-dropdown">
            <Link
              to="/profile"
              className={`nav-link profile-btn ${
                pathname.startsWith("/profile") ? "active" : ""
              }`}
            >
              <div className="user-avatar">
                {userName.charAt(0).toUpperCase()}
              </div>

              <span className="user-name">{userName}</span>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
}
