import React, { useState } from 'react';

function RegistrationForm({ onRegister }) {
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!login || !password) {
      setError('Login and password cannot be empty.');
      return;
    }

    if (password.length < 8) {
      setError('Password must be at least 8 characters long.');
      return;
    }

    try {
      await onRegister(login, password);
    } catch (err) {
      setError(err.message || 'Registration failed.');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="registration-form">
      <h2>Register</h2>
      {error && <p className="error-message">{error}</p>}
      <div>
        <label htmlFor="login">Login:</label>
        <input
          type="text"
          id="login"
          value={login}
          onChange={(e) => setLogin(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="password">Password:</label>
        <input
          type="password"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <button type="submit">Register</button>
    </form>
  );
}

export default RegistrationForm;