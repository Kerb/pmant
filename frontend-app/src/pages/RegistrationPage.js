import React from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import RegistrationForm from '../components/RegistrationForm';
import { register } from '../services/authService';

function RegistrationPage({ setIsLoggedIn }) {
  const navigate = useNavigate();

  const handleRegister = async (login, password) => {
    try {
      const response = await register(login, password);
      console.log('Registration successful:', response);
      setIsLoggedIn(true); // Set isLoggedIn to true on successful registration
      navigate('/');
    } catch (error) {
      console.error('Registration failed:', error.message);
      throw error;
    }
  };

  return (
    <div className="registration-page">
      <RegistrationForm onRegister={handleRegister} />
    </div>
  );
}

export default RegistrationPage;