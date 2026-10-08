import React, { useState } from 'react';
import { Camera, MapPin, Upload, AlertCircle, CheckCircle } from 'lucide-react';

export default function CitizenDashboard() {
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    // Simulate API call
    setTimeout(() => {
      setIsSubmitting(false);
      setSuccess(true);
      setDescription('');
      setLocation('');
      setTimeout(() => setSuccess(false), 3000);
    }, 1500);
  };

  return (
    <div className="animate-fade-in" style={{ padding: '20px 0' }}>
      <div style={{ marginBottom: '32px' }}>
        <h1 style={{ marginBottom: '8px' }}>Citizen Dashboard</h1>
        <p style={{ color: 'var(--text-muted)' }}>Help keep our city clean by reporting waste instances.</p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>
        <div className="glass-panel" style={{ padding: '32px' }}>
          <h2 style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '24px' }}>
            <AlertCircle size={24} color="var(--primary)" />
            Report New Issue
          </h2>
          {success && (
            <div style={{ padding: '16px', background: 'rgba(16, 185, 129, 0.1)', color: 'var(--primary)', borderRadius: 'var(--radius-md)', marginBottom: '24px', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <CheckCircle size={20} /> Successfully submitted report!
            </div>
          )}
          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <label className="input-label">Location (Area / Address)</label>
              <div style={{ position: 'relative' }}>
                <MapPin size={18} style={{ position: 'absolute', top: '14px', left: '16px', color: 'var(--text-muted)' }} />
                <input required type="text" className="input-field" placeholder="e.g. Central Park North Gate" value={location} onChange={(e) => setLocation(e.target.value)} style={{ paddingLeft: '44px', width: '100%' }} />
              </div>
            </div>

            <div className="input-group">
              <label className="input-label">Description</label>
              <textarea required className="input-field" placeholder="Describe the type of waste, estimated amount..." value={description} onChange={(e) => setDescription(e.target.value)} style={{ minHeight: '100px', resize: 'vertical' }} />
            </div>

            <div className="input-group">
              <label className="input-label">Upload Image</label>
              <div style={{ border: '2px dashed var(--border-color)', borderRadius: 'var(--radius-md)', padding: '40px', textAlign: 'center', cursor: 'pointer', transition: 'var(--transition)' }} onMouseOver={(e) => e.currentTarget.style.borderColor = 'var(--primary)'} onMouseOut={(e) => e.currentTarget.style.borderColor = 'var(--border-color)'}>
                <Camera size={32} style={{ color: 'var(--text-muted)', marginBottom: '12px' }} />
                <p style={{ fontWeight: 500 }}>Click or drag image to upload</p>
                <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '4px' }}>PNG, JPG up to 10MB</p>
              </div>
            </div>

            <button type="submit" className="btn btn-primary" style={{ width: '100%', padding: '14px' }} disabled={isSubmitting}>
              {isSubmitting ? 'Analyzing Image...' : <><Upload size={20} /> Submit Report</>}
            </button>
          </form>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          <div className="glass-panel" style={{ padding: '24px' }}>
            <h2>Your Recent Reports</h2>
            <div style={{ marginTop: '24px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
              
              <div style={{ padding: '16px', background: 'var(--surface)', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <h4 style={{ marginBottom: '4px' }}>Downtown Market Square</h4>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Illegal dumping • Oct 5, 2026</p>
                </div>
                <span className="badge badge-success">CLEANED</span>
              </div>

              <div style={{ padding: '16px', background: 'var(--surface)', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <h4 style={{ marginBottom: '4px' }}>Riverside Park Walkway</h4>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Plastic waste accumulation • Oct 7, 2026</p>
                </div>
                <span className="badge badge-warning">IN PROGRESS</span>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
