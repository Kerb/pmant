import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';

const App = () => {
  const [message, setMessage] = useState("Hello from React Frontend!");
  const [loading, setLoading] = useState(false);

  const handlePing = async () => {
    setLoading(true);
    setMessage("Loading..."); // FR-008: Display "Loading..." text
    try {
      const response = await fetch("/api/ping"); // FR-004: Send HTTP request to backend
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.text(); // Assuming backend returns plain text "pong"
      setMessage(data);
    } catch (error) {
      console.error("Ping failed:", error);
      setMessage("Error: Could not connect to backend."); // FR-007: Display error message
    } finally {
      setLoading(false);
    }
  };

  return (
    <React.StrictMode>
      <h1>{message}</h1>
      <button onClick={handlePing} disabled={loading}>
        {loading ? "Pinging..." : "Ping"}
      </button>
    </React.StrictMode>
  );
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);