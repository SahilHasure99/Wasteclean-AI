import React, { useState, useRef } from 'react';
import { Camera, MapPin, Upload, AlertCircle, CheckCircle, X } from 'lucide-react';

export default function CitizenDashboard() {
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [imagePreview, setImagePreview] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const fileInputRef = useRef(null);

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const clearImage = () => {
    setImagePreview(null);
    if (fileInputRef.current) fileInputRef.current.value = '';
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!imagePreview) return alert('Please upload an image of the waste.');
    setIsSubmitting(true);
    // Simulate API call to AI service
    setTimeout(() => {
      setIsSubmitting(false);
      setSuccess(true);
      setDescription('');
      setLocation('');
      clearImage();
      setTimeout(() => setSuccess(false), 4000);
    }, 2000);
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
            <div className="animate-fade-in" style={{ padding: '16px', background: 'rgba(16, 185, 129, 0.15)', color: 'var(--primary)', borderRadius: 'var(--radius-md)', marginBottom: '24px', display: 'flex', alignItems: 'center', gap: '8px', border: '1px solid var(--primary-light)' }}>
              <CheckCircle size={20} /> Successfully submitted report to City AI!
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
              <label className="input-label">Upload Image of Waste</label>
              
              {!imagePreview ? (
                <div 
                  onClick={() => fileInputRef.current.click()}
                  style={{ border: '2px dashed var(--border-color)', borderRadius: 'var(--radius-md)', padding: '40px', textAlign: 'center', cursor: 'pointer', transition: 'var(--transition)', background: 'var(--surface-glass)' }} 
                  onMouseOver={(e) => e.currentTarget.style.borderColor = 'var(--primary)'} 
                  onMouseOut={(e) => e.currentTarget.style.borderColor = 'var(--border-color)'}
                >
                  <Camera size={32} style={{ color: 'var(--text-muted)', marginBottom: '12px' }} />
                  <p style={{ fontWeight: 500 }}>Click to select an image from your device</p>
                  <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '4px' }}>PNG, JPG acceptable format</p>
                </div>
              ) : (
                <div style={{ position: 'relative', borderRadius: 'var(--radius-md)', overflow: 'hidden', border: '1px solid var(--border-color)' }}>
                  <img src={imagePreview} alt="Preview" style={{ width: '100%', height: '200px', objectFit: 'cover', display: 'block' }} />
                  <button type="button" onClick={clearImage} className="btn" style={{ position: 'absolute', top: '8px', right: '8px', background: 'var(--surface)', color: 'var(--text-main)', padding: '6px', borderRadius: '50%' }}>
                    <X size={16} />
                  </button>
                </div>
              )}
              {/* Hidden genuine file input */}
              <input type="file" ref={fileInputRef} accept="image/png, image/jpeg" style={{ display: 'none' }} onChange={handleImageUpload} />
            </div>

            <button type="submit" className="btn btn-primary" style={{ width: '100%', padding: '14px', marginTop: '8px' }} disabled={isSubmitting || !imagePreview}>
              {isSubmitting ? 'Analyzing AI Image...' : <><Upload size={20} /> Submit Report</>}
            </button>
          </form>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          <div className="glass-panel" style={{ padding: '24px' }}>
            <h2>Your Recent Reports</h2>
            <div style={{ marginTop: '24px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
              
              <div style={{ padding: '16px', background: 'var(--surface-glass)', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <h4 style={{ marginBottom: '4px' }}>Downtown Market Square</h4>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Illegal dumping • Oct 5, 2026</p>
                </div>
                <span className="badge badge-success">CLEANED</span>
              </div>

              <div style={{ padding: '16px', background: 'var(--surface-glass)', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <h4 style={{ marginBottom: '4px' }}>Riverside Park Walkway</h4>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Plastic waste • Oct 7, 2026</p>
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
