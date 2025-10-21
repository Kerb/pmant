import React from 'react';
import RegistrationForm from '../components/RegistrationForm';
import { register } from '../services/authService';

function RegistrationPage() {
  const handleRegister = async (login, password) => {
    try {
      const response = await register(login, password);
      console.log('Registration successful:', response);
      alert('Registration successful!');
      // Here you would typically redirect the user or update global state
    } catch (error) {
      console.error('Registration failed:', error.message);
      throw error; // Re-throw to be caught by the form component
    }
  };

  return (
    <div className="registration-page">
      <RegistrationForm onRegister={handleRegister} />
    </div>
  );
}

export default RegistrationPage;