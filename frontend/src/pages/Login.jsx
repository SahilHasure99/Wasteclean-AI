import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { LogIn, CircleUser, AlertCircle } from 'lucide-react';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('CITIZEN');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const validatePassword = (pwd) => {
    // Exactly 8 characters, at least one letter and at least one number
    const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8}$/;
    return regex.test(pwd);
  };

  const handleLogin = (e) => {
    e.preventDefault();
    setError('');

    if (!validatePassword(password)) {
      setError('Password must be exactly 8 characters containing both letters and numbers.');
      return;
    }

    // Assign role to localStorage so App.jsx picks it up
    localStorage.setItem('role', role);
    window.dispatchEvent(new Event('auth-change'));
    
    if (role === 'ADMIN') {
      navigate('/admin');
    } else {
      navigate('/citizen');
    }
  };

  return (
    <div className="animate-fade-in" style={{ maxWidth: '400px', margin: '60px auto' }}>
      <div className="glass-panel" style={{ padding: '40px' }}>
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{ display: 'inline-flex', padding: '16px', background: 'var(--primary-light)', borderRadius: '50%', color: 'var(--primary)', marginBottom: '16px' }}>
            <CircleUser size={48} />
          </div>
          <h2>Welcome Back</h2>
          <p style={{ color: 'var(--text-muted)' }}>Secure Smart City Login</p>
        </div>

        {error && (
          <div style={{ padding: '12px', background: 'rgba(239, 68, 68, 0.1)', color: 'var(--danger)', borderRadius: 'var(--radius-sm)', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.85rem', fontWeight: 500 }}>
            <AlertCircle size={18} /> {error}
          </div>
        )}

        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label className="input-label">Role</label>
            <select className="input-field" value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="CITIZEN">Citizen (Waste Reporter)</option>
              <option value="ADMIN">City Administrator</option>
            </select>
          </div>
          <div className="input-group">
            <label className="input-label">Email</label>
            <input type="email" required className="input-field" placeholder="Enter your email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div className="input-group">
            <label className="input-label">Password (8 chars, alphanumeric)</label>
            <input type="password" required className="input-field" placeholder="••••••••" value={password} onChange={(e) => setPassword(e.target.value)} />
          </div>
          
          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '16px', padding: '14px' }}>
            <LogIn size={20} /> Sign In
          </button>
        </form>
      </div>
    </div>
  );
}
