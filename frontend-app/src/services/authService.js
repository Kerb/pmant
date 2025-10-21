const API_BASE_URL = '/api';

export const register = async (login, password) => {
  const response = await fetch(`${API_BASE_URL}/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ login, password }),
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.error || 'Registration failed');
  }

  // Assuming successful registration also logs the user in and returns some user data
  // For now, we'll just return the data from the backend
  const loginResponse = await login(login, password); // Automatically log in after registration
  return loginResponse;
};

export const login = async (login, password) => {
  // Placeholder for login functionality
  console.log('Attempting to log in user:', login);
  return { message: 'Login successful (placeholder)', userId: 'mock-user-id' };
};