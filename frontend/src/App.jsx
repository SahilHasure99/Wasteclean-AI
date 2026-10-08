import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import CitizenDashboard from './pages/CitizenDashboard';
import AdminDashboard from './pages/AdminDashboard';
import Chatbot from './components/Chatbot';
import './index.css';

// Protected Route Wrapper
const ProtectedRoute = ({ children, allowedRole, currentRole }) => {
  if (!currentRole) return <Navigate to="/login" replace />;
  if (currentRole !== allowedRole) {
    // If they have the wrong role, redirect them to their correct dashboard
    return <Navigate to={currentRole === 'ADMIN' ? '/admin' : '/citizen'} replace />;
  }
  return children;
};

function App() {
  const [role, setRole] = useState(localStorage.getItem('role'));

  // Listen to login/logout changes
  useEffect(() => {
    const handleStorageChange = () => {
      setRole(localStorage.getItem('role'));
    };
    window.addEventListener('storage', handleStorageChange);
    // Custom event for same-tab updates
    window.addEventListener('auth-change', handleStorageChange);
    return () => {
      window.removeEventListener('storage', handleStorageChange);
      window.removeEventListener('auth-change', handleStorageChange);
    };
  }, []);

  return (
    <BrowserRouter>
      <Navbar role={role} />
      <main className="app-container">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />
          
          <Route 
            path="/citizen" 
            element={
              <ProtectedRoute allowedRole="CITIZEN" currentRole={role}>
                <CitizenDashboard />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/admin" 
            element={
              <ProtectedRoute allowedRole="ADMIN" currentRole={role}>
                <AdminDashboard />
              </ProtectedRoute>
            } 
          />
        </Routes>
      </main>
      
      {/* Global AI Chatbot floating in the background */}
      {role && <Chatbot />}
    </BrowserRouter>
  );
}

export default App;
