// src/components/SidebarAuth.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/sidebar.css';

export default function SidebarAuth({ open, onClose }) {
  /* ---------- estado ---------- */
  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState('');
  const [loginData, setLoginData]       = useState({ email: '', password: '' });
  const [registerData, setRegisterData] = useState({
    nombre: '', email: '', password: '', confirmPassword: '',
    direccion: '', telefono: '',
  });

  const navigate = useNavigate();
  const API = (import.meta.env.VITE_API_URL || '').replace(/\/$/, '');
  
  /* ---------- helpers ---------- */
  const handleChange = setter => e =>
    setter(p => ({ ...p, [e.target.name]: e.target.value }));

  /* ---------- login ---------- */
  const handleLogin = async e => {
    e.preventDefault(); setLoading(true); setError('');
    try {
      const res = await fetch(`${API}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginData),
      });
      if (!res.ok) throw new Error('Credenciales inválidas');
      const { accessToken, refreshToken } = await res.json();
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      onClose(); navigate('/');
    } catch (err) { setError(err.message); }
    finally { setLoading(false); }
  };

  /* ---------- registro ---------- */
  const handleRegister = async e => {
    e.preventDefault();
    if (registerData.password !== registerData.confirmPassword)
      return setError('Las contraseñas no coinciden');

    setLoading(true); setError('');
    try {
      const res = await fetch(`${API}/api/clientes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password})
      });
      if (!res.ok) throw new Error('Email ya registrado o datos inválidos');
      alert('Registro exitoso - inicia sesión');
      setIsLogin(true);
    } catch (err) { setError(err.message); }
    finally { setLoading(false); }
  };

  /* ---------- UI ---------- */
  return (
    <>
      {/* Backdrop con clase .open para activar transición */}
      <div
        className={`sidebar-backdrop ${open ? 'open' : ''}`}
        onClick={onClose}
      />

      <aside className={`sidebar-auth ${open ? 'open' : ''}`}>
        <button className="sidebar-close" onClick={onClose}>×</button>

        {isLogin ? (
          <>
            <h2>Iniciar Sesión</h2>
            <form onSubmit={handleLogin}>
              <input
                name="email" type="email" placeholder="Email"
                value={loginData.email} onChange={handleChange(setLoginData)} required
              />
              <input
                name="password" type="password" placeholder="Contraseña"
                value={loginData.password} onChange={handleChange(setLoginData)} required
              />
              {error && <p className="error">{error}</p>}
              <button type="submit" disabled={loading}>
                {loading ? 'Enviando…' : 'Entrar'}
              </button>
            </form>

            <p className="toggle">
              ¿No tienes cuenta?{' '}
              <button
                type="button"
                className="sidebar-link"
                onClick={() => { setIsLogin(false); setError(''); }}
              >
                Regístrate
              </button>
            </p>
          </>
        ) : (
          <>
            <h2>Registro</h2>
            <form onSubmit={handleRegister}>
              <input name="nombre"  placeholder="Nombre completo"
                     value={registerData.nombre}
                     onChange={handleChange(setRegisterData)} required />
              <input name="email"   type="email" placeholder="Email"
                     value={registerData.email}
                     onChange={handleChange(setRegisterData)} required />
              <input name="password" type="password" placeholder="Contraseña"
                     value={registerData.password}
                     onChange={handleChange(setRegisterData)} required />
              <input name="confirmPassword" type="password"
                     placeholder="Confirmar contraseña"
                     value={registerData.confirmPassword}
                     onChange={handleChange(setRegisterData)} required />
              <input name="direccion" placeholder="Dirección"
                     value={registerData.direccion}
                     onChange={handleChange(setRegisterData)} required />
              <input name="telefono" placeholder="Teléfono"
                     value={registerData.telefono}
                     onChange={handleChange(setRegisterData)} required />
              {error && <p className="error">{error}</p>}
              <button type="submit" disabled={loading}>
                {loading ? 'Enviando…' : 'Registrar'}
              </button>
            </form>

            <p className="toggle">
              ¿Ya tienes cuenta?{' '}
              <button
                type="button"
                className="sidebar-link"
                onClick={() => { setIsLogin(true); setError(''); }}
              >
                Inicia sesión
              </button>
            </p>
          </>
        )}
      </aside>
    </>
  );
}
