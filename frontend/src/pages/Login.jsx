import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { LogIn, User, CircleUser } from 'lucide-react';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('CITIZEN'); // default for prototype
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
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
          <p style={{ color: 'var(--text-muted)' }}>Sign in to continue to WasteWise AI</p>
        </div>

        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label className="input-label">Role</label>
            <select className="input-field" value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="CITIZEN">Citizen</option>
              <option value="ADMIN">City Administrator</option>
            </select>
          </div>
          <div className="input-group">
            <label className="input-label">Email</label>
            <input type="email" required className="input-field" placeholder="Enter your email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div className="input-group">
            <label className="input-label">Password</label>
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
