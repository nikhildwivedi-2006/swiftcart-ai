import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  FiSend,
  FiMessageCircle,
  FiShoppingBag,
  FiCreditCard,
  FiRefreshCw,
} from "react-icons/fi";
import { askAI } from "../services/aiService";
import "./Support.css";

const WELCOME_MSG = {
  sender: "ai",
  text: "👋 Hi! I'm SwiftCart AI Assistant.\n\nAsk anything you want about orders, payments, delivery, returns or products.",
  userQuestion: null, // no button on welcome
};

const getActionButton = (userQuestion, navigate) => {
  if (!userQuestion) return null;
  const q = userQuestion.toLowerCase();

  if (
    q.includes("order") ||
    q.includes("track") ||
    q.includes("delivery") ||
    q.includes("shipping") ||
    q.includes("where is my")
  ) {
    return (
      <button className="action-btn" onClick={() => navigate("/orders")}>
        <FiShoppingBag size={15} />
        View My Orders
      </button>
    );
  }

  if (q.includes("payment") || q.includes("refund")) {
    return (
      <button className="action-btn" onClick={() => navigate("/orders")}>
        <FiCreditCard size={15} />
        View Payments
      </button>
    );
  }

  if (q.includes("return")) {
    return (
      <button className="action-btn" onClick={() => navigate("/orders")}>
        <FiRefreshCw size={15} />
        Return Eligible Orders
      </button>
    );
  }

  if (
    q.includes("product") ||
    q.includes("recommend") ||
    q.includes("browse")
  ) {
    return (
      <button className="action-btn" onClick={() => navigate("/products")}>
        <FiShoppingBag size={15} />
        Browse Products
      </button>
    );
  }

  return null;
};

export default function Support() {
  const navigate = useNavigate();

  const [messages, setMessages] = useState([WELCOME_MSG]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [showChips, setShowChips] = useState(true);

  const bottomRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, loading]);

  const suggestions = [
    "Where is my order?",
    "Payment issue",
    "Return policy",
    "Recommend products",
  ];

  const sendMessage = async (text) => {
    if (!text.trim() || loading) return;

    setMessages((prev) => [...prev, { sender: "user", text }]);
    setInput("");
    setShowChips(false);
    setLoading(true);

    try {
      const res = await askAI(text);
      setMessages((prev) => [
        ...prev,
        { sender: "ai", text: res.response, userQuestion: text },
      ]);
    } catch {
      setMessages((prev) => [
        ...prev,
        {
          sender: "ai",
          text: "Sorry, I'm having trouble connecting. Please try again in a moment.",
          userQuestion: null,
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleSend = () => sendMessage(input);

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="support-page">
      <div className="chat-card">
        <div className="chat-header">
          <div className="chat-title">
            <FiMessageCircle size={28} />
            <div>
              <h2>SwiftCart AI Support</h2>
              <p>Ask Anything You Want</p>
            </div>
          </div>
        </div>

        {showChips && (
          <div className="quick-section">
            <p>Quick Questions</p>
            <div className="chips">
              {suggestions.map((item) => (
                <button
                  key={item}
                  className="chip"
                  onClick={() => sendMessage(item)}
                >
                  {item}
                </button>
              ))}
            </div>
          </div>
        )}

        <div className="chat-body">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`message ${msg.sender === "user" ? "user-message" : "ai-message"}`}
            >
              <div className="bubble">
                {msg.text}
                {msg.sender === "ai" &&
                  getActionButton(msg.userQuestion, navigate)}
              </div>
            </div>
          ))}

          {loading && (
            <div className="message ai-message">
              <div className="bubble thinking">
                <span className="dot" />
                <span className="dot" />
                <span className="dot" />
              </div>
            </div>
          )}

          <div ref={bottomRef} />
        </div>

        <div className="chat-input">
          <textarea
            rows="2"
            placeholder="Ask anything..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
          />
          <button onClick={handleSend} disabled={loading}>
            <FiSend size={20} />
          </button>
        </div>
      </div>
    </div>
  );
}
