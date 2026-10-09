import React, { useState } from 'react';
import { MessageSquare, X, Send, Bot } from 'lucide-react';

export default function Chatbot() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    { role: 'ai', text: 'Hello! I am your AI Assistant. How can I help you navigate the system today?' }
  ]);
  const [input, setInput] = useState('');

  const handleSend = (e) => {
    e.preventDefault();
    if (!input.trim()) return;
    
    // Add User message
    const newMsgs = [...messages, { role: 'user', text: input }];
    setMessages(newMsgs);
    setInput('');

    // Mock AI Response
    setTimeout(() => {
      setMessages([...newMsgs, { 
        role: 'ai', 
        text: 'I am a demo AI. In the production app, my brain will connect to the backend models to analyze your specific city metrics!'
      }]);
    }, 1000);
  };

  return (
    <>
      {/* Floating Action Button */}
      <div style={{ position: 'fixed', bottom: '32px', right: '32px', zIndex: 900 }}>
        {!isOpen && (
          <button onClick={() => setIsOpen(true)} className="btn btn-primary" style={{ height: '60px', width: '60px', borderRadius: '50%', padding: '0', display: 'flex', justifyContent: 'center', boxShadow: 'var(--shadow-lg)' }}>
            <MessageSquare size={28} />
          </button>
        )}
      </div>

      {/* Chat Windows (Modal) */}
      {isOpen && (
        <div className="glass-panel animate-fade-in" style={{ position: 'fixed', bottom: '100px', right: '32px', width: '350px', height: '450px', zIndex: 900, display: 'flex', flexDirection: 'column', overflow: 'hidden', boxShadow: 'var(--shadow-lg)' }}>
          {/* Header */}
          <div style={{ background: 'var(--primary)', padding: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', color: 'white' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Bot size={24} />
              <h3 style={{ margin: 0, fontSize: '1.1rem' }}>AI Assistant</h3>
            </div>
            <button onClick={() => setIsOpen(false)} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer' }}>
              <X size={20} />
            </button>
          </div>

          {/* Messages Area */}
          <div style={{ flex: 1, padding: '16px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '12px', background: 'var(--surface-glass)' }}>
            {messages.map((msg, i) => (
              <div key={i} style={{ alignSelf: msg.role === 'user' ? 'flex-end' : 'flex-start', maxWidth: '80%', padding: '12px 16px', borderRadius: 'var(--radius-lg)', background: msg.role === 'user' ? 'var(--primary)' : 'var(--surface)', color: msg.role === 'user' ? 'white' : 'var(--text-main)', border: msg.role === 'user' ? 'none' : '1px solid var(--border-color)', fontSize: '0.9rem' }}>
                {msg.text}
              </div>
            ))}
          </div>

          {/* Input Area */}
          <form style={{ padding: '16px', borderTop: '1px solid var(--border-color)', display: 'flex', gap: '8px', background: 'var(--surface)' }} onSubmit={handleSend}>
            <input 
              type="text" 
              className="input-field" 
              placeholder="Ask me anything..." 
              style={{ flex: 1, margin: 0, padding: '8px 12px', fontSize: '0.9rem' }} 
              value={input} 
              onChange={(e) => setInput(e.target.value)} 
            />
            <button className="btn btn-primary" style={{ padding: '8px', borderRadius: '50%' }} type="submit">
              <Send size={18} />
            </button>
          </form>
        </div>
      )}
    </>
  );
}
