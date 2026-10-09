// Example API integration for connecting to the Spring Boot backend
const BASE_URL = 'http://localhost:8080/api';

export const login = async (email, password) => {
  try {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    if (!response.ok) throw new Error('Login failed');
    const data = await response.json();
    localStorage.setItem('token', data.token);
    return data;
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};

export const reportIssue = async (location, description, imageFile) => {
  try {
    const formData = new FormData();
    formData.append('location', location);
    formData.append('description', description);
    if (imageFile) formData.append('image', imageFile);

    const token = localStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/reports`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: formData
    });
    if (!response.ok) throw new Error('Failed to report issue');
    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};
