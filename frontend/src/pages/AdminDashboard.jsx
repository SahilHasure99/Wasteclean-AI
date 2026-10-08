import React from 'react';
import { BarChart3, AlertTriangle, Users, Map, CheckCircle2, TrendingUp, AlertOctagon } from 'lucide-react';

export default function AdminDashboard() {
  const kpis = [
    { label: 'Active Reports', value: '1,248', icon: AlertTriangle, trend: '+12%', color: 'var(--accent)' },
    { label: 'Cleaned This Week', value: '842', icon: CheckCircle2, trend: '+5%', color: 'var(--primary)' },
    { label: 'Active Citizens', value: '4,103', icon: Users, trend: '+18%', color: 'var(--secondary)' },
    { label: 'Risk Areas', value: '7', icon: AlertOctagon, trend: '-2', color: 'var(--danger)' }
  ];

  const tasks = [
    { id: 'TASK-1029', area: 'Downtown Sector A', severity: 'CRITICAL', status: 'PENDING', aiAnalysis: 'High probability of localized outbreak.' },
    { id: 'TASK-1030', area: 'Riverside Walk', severity: 'HIGH', status: 'IN_PROGRESS', aiAnalysis: 'Persistent illegal dumping detected.' },
    { id: 'TASK-1031', area: 'North Gate Park', severity: 'MEDIUM', status: 'ASSIGNED', aiAnalysis: 'Standard accumulation.' },
  ];

  return (
    <div className="animate-fade-in" style={{ padding: '20px 0' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '32px' }}>
        <div>
          <h1 style={{ marginBottom: '8px' }}>City Command Center</h1>
          <p style={{ color: 'var(--text-muted)' }}>AI-powered overview of city-wide sanitation metrics.</p>
        </div>
        <button className="btn btn-primary"><Map size={18} /> View Live Map</button>
      </div>

      {/* KPI Section */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '24px', marginBottom: '32px' }}>
        {kpis.map((kpi, i) => (
          <div key={i} className="glass-panel" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div style={{ padding: '12px', background: 'rgba(255,255,255,0.5)', borderRadius: '50%', color: kpi.color }}>
                <kpi.icon size={24} />
              </div>
              <span style={{ fontSize: '0.85rem', fontWeight: 600, color: kpi.trend.startsWith('+') ? 'var(--primary)' : 'var(--primary)' }}>
                {kpi.trend}
              </span>
            </div>
            <div>
              <h2 style={{ fontSize: '2rem', margin: '8px 0 4px 0' }}>{kpi.value}</h2>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', fontWeight: 500 }}>{kpi.label}</p>
            </div>
          </div>
        ))}
      </div>

      {/* Priority Engine Tasks */}
      <div className="glass-panel" style={{ padding: '32px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
          <h2 style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <TrendingUp size={24} color="var(--primary)" /> AI Priority Tasks
          </h2>
          <button className="btn btn-secondary" style={{ padding: '6px 12px', fontSize: '0.8rem' }}>View All</button>
        </div>

        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                <th style={{ padding: '16px 8px', fontWeight: 600 }}>Task ID</th>
                <th style={{ padding: '16px 8px', fontWeight: 600 }}>Area</th>
                <th style={{ padding: '16px 8px', fontWeight: 600 }}>AI Analysis</th>
                <th style={{ padding: '16px 8px', fontWeight: 600 }}>Severity</th>
                <th style={{ padding: '16px 8px', fontWeight: 600 }}>Status</th>
                <th style={{ padding: '16px 8px', fontWeight: 600, textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {tasks.map(task => (
                <tr key={task.id} style={{ borderBottom: '1px solid var(--border-color)', transition: 'var(--transition)' }} className="table-row-hover">
                  <td style={{ padding: '16px 8px', fontWeight: 500 }}>{task.id}</td>
                  <td style={{ padding: '16px 8px' }}>{task.area}</td>
                  <td style={{ padding: '16px 8px', color: 'var(--text-muted)', fontSize: '0.9rem' }}>{task.aiAnalysis}</td>
                  <td style={{ padding: '16px 8px' }}>
                    <span className={`badge badge-${task.severity === 'CRITICAL' ? 'danger' : task.severity === 'HIGH' ? 'warning' : 'success'}`}>
                      {task.severity}
                    </span>
                  </td>
                  <td style={{ padding: '16px 8px' }}>
                    <span style={{ fontSize: '0.85rem', fontWeight: 600 }}>{task.status.replace('_', ' ')}</span>
                  </td>
                  <td style={{ padding: '16px 8px', textAlign: 'right' }}>
                    <button className="btn btn-secondary" style={{ padding: '6px 12px', fontSize: '0.8rem' }}>Dispatch</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
