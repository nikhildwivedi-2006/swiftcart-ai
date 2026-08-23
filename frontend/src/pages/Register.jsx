import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import {
  FiMail,
  FiLock,
  FiEye,
  FiEyeOff,
  FiShoppingCart,
  FiUser,
  FiPhone,
} from "react-icons/fi";

import { registerUser } from "../services/authService";
import { useUser } from "../context/UserContext";
import "./Auth.css";

export default function Register() {
  const navigate = useNavigate();
  const { selectUser } = useUser();

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    contactNumber: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.name || !form.email || !form.password || !form.contactNumber) {
      setError("Please fill in all fields.");
      return;
    }

    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    if (form.contactNumber.length !== 10 || !/^\d+$/.test(form.contactNumber)) {
      setError("Enter a valid 10-digit contact number.");
      return;
    }

    setLoading(true);

    try {
      const res = await registerUser(
        form.name,
        form.email,
        form.password,
        form.contactNumber,
      );

      console.log("REGISTER RESPONSE:", res.data);

      localStorage.setItem("token", res.data.token);

      localStorage.setItem("user", JSON.stringify(res.data));

      // update React Context
      selectUser(res.data);

      navigate("/");
    } catch (err) {
      setError(err.message || "Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-logo">
          <FiShoppingCart size={28} />
          <span>SwiftCart</span>
        </div>

        <h2 className="auth-title">Create your account</h2>

        <p className="auth-subtitle">
          Join SwiftCart and start shopping smarter
        </p>

        {error && <div className="auth-error">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="auth-field">
            <label>Full Name</label>

            <div className="auth-input-wrap">
              <FiUser className="auth-input-icon" />

              <input
                type="text"
                name="name"
                placeholder="Nikhil Kumar"
                value={form.name}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="auth-field">
            <label>Email Address</label>

            <div className="auth-input-wrap">
              <FiMail className="auth-input-icon" />

              <input
                type="email"
                name="email"
                placeholder="you@example.com"
                value={form.email}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="auth-field">
            <label>Contact Number</label>

            <div className="auth-input-wrap">
              <FiPhone className="auth-input-icon" />

              <input
                type="tel"
                name="contactNumber"
                placeholder="10-digit mobile number"
                value={form.contactNumber}
                onChange={handleChange}
                maxLength={10}
              />
            </div>
          </div>

          <div className="auth-field">
            <label>Password</label>

            <div className="auth-input-wrap">
              <FiLock className="auth-input-icon" />

              <input
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="Create a password"
                value={form.password}
                onChange={handleChange}
              />

              <button
                type="button"
                className="auth-eye"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? <FiEyeOff /> : <FiEye />}
              </button>
            </div>
          </div>

          <div className="auth-field">
            <label>Confirm Password</label>

            <div className="auth-input-wrap">
              <FiLock className="auth-input-icon" />

              <input
                type={showConfirm ? "text" : "password"}
                name="confirmPassword"
                placeholder="Re-enter your password"
                value={form.confirmPassword}
                onChange={handleChange}
              />

              <button
                type="button"
                className="auth-eye"
                onClick={() => setShowConfirm(!showConfirm)}
              >
                {showConfirm ? <FiEyeOff /> : <FiEye />}
              </button>
            </div>
          </div>

          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? "Creating account..." : "Create Account"}
          </button>
        </form>

        <p className="auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
