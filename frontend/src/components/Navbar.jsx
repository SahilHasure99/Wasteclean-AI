import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Leaf, LogOut } from 'lucide-react';

export default function Navbar({ role }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('role');
    localStorage.removeItem('token');
    window.dispatchEvent(new Event('auth-change'));
    navigate('/login');
  };

  return (
    <header className="glass-panel" style={{ borderRadius: 0, borderTop: 0, borderLeft: 0, borderRight: 0, padding: '16px 32px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', position: 'sticky', top: 0, zIndex: 100 }}>
      <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '8px', textDecoration: 'none', color: 'var(--primary)', fontWeight: 700, fontSize: '1.25rem', fontFamily: 'var(--font-heading)' }}>
        <Leaf size={28} />
        <span style={{ background: 'var(--surface-glass)', padding: '2px 8px', borderRadius: '4px' }}>WasteWise AI</span>
      </Link>
      
      {role && (
        <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
          <span style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-muted)' }}>
            Logged in as: {role}
          </span>
          <button onClick={handleLogout} className="btn" style={{ background: 'var(--danger)', color: 'white', padding: '8px 16px', fontSize: '0.85rem' }}>
            <LogOut size={16} /> Logout
          </button>
        </div>
      )}
    </header>
  );
}
