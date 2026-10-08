import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Leaf, LogOut, LayoutDashboard } from 'lucide-react';

export default function Navbar() {
  const navigate = useNavigate();
  // Simplified auth state for prototype
  const isLoggedIn = window.location.pathname !== '/login';

  const handleLogout = () => {
    navigate('/login');
  };

  return (
    <header className="glass-panel" style={{ borderRadius: 0, borderTop: 0, borderLeft: 0, borderRight: 0, padding: '16px 32px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', position: 'sticky', top: 0, zIndex: 100 }}>
      <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '8px', textDecoration: 'none', color: 'var(--primary)', fontWeight: 700, fontSize: '1.25rem', fontFamily: 'var(--font-heading)' }}>
        <Leaf size={28} />
        WasteWise AI
      </Link>
      
      {isLoggedIn && (
        <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
          <Link to="/citizen" className="btn btn-secondary" style={{ padding: '8px 16px', fontSize: '0.85rem' }}>
            <LayoutDashboard size={16} /> Citizen View
          </Link>
          <Link to="/admin" className="btn btn-secondary" style={{ padding: '8px 16px', fontSize: '0.85rem' }}>
            <LayoutDashboard size={16} /> Admin View
          </Link>
          <button onClick={handleLogout} className="btn" style={{ background: 'var(--danger)', color: 'white', padding: '8px 16px', fontSize: '0.85rem' }}>
            <LogOut size={16} /> Logout
          </button>
        </div>
      )}
    </header>
  );
}
