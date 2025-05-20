import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../css/sidebar.css'   // <-- tu nuevo CSS para el sidebar

export default function SidebarAuth({ open, onClose }) {
  const [isLogin, setIsLogin] = useState(true)
  const [loginData, setLoginData] = useState({ email: '', password: '' })
  const [registerData, setRegisterData] = useState({
    nombre: '',
    email: '',
    password: '',
    confirmPassword: '',
    direccion: '',
    telefono: '',
  })
  const navigate = useNavigate()
  const API_URL = import.meta.env.VITE_API_URL

  // Manejo de cambios en el form de login
  const handleLoginChange = e => {
    setLoginData(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  // Envío del form de login
  const handleLogin = async e => {
    e.preventDefault()
    try {
      const res = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: loginData.email,
          password: loginData.password,
        }),
      })
      if (!res.ok) throw new Error()
      const { accessToken, refreshToken } = await res.json()
      localStorage.setItem('accessToken', accessToken)
      localStorage.setItem('refreshToken', refreshToken)
      onClose()
      navigate('/')
    } catch {
      alert('Credenciales inválidas')
    }
  }

  // Manejo de cambios en el form de registro
  const handleRegisterChange = e => {
    setRegisterData(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  // Envío del form de registro
  const handleRegister = async e => {
    e.preventDefault()
    if (registerData.password !== registerData.confirmPassword) {
      alert('Las contraseñas no coinciden')
      return
    }
    try {
      const res = await fetch(`${API_URL}/clientes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nombreCompleto: registerData.nombre,
          email: registerData.email,
          password: registerData.password,
          direccion: registerData.direccion,
          telefono: registerData.telefono,
        }),
      })
      if (!res.ok) throw new Error()
      await res.json()
      alert('Registro exitoso')
      setIsLogin(true)
    } catch {
      alert('Error al registrar usuario')
    }
  }

  return (
    <>
      {/* Backdrop */}
      {open && <div className="sidebar-backdrop" onClick={onClose} />}

      {/* Sidebar */}
      <aside className={`sidebar-auth ${open ? 'open' : ''}`}>
        <button className="sidebar-close" onClick={onClose}>×</button>

        {isLogin ? (
          <>
            <h2>Iniciar Sesión</h2>
            <form onSubmit={handleLogin}>
              <label>Email</label>
              <input
                type="email"
                name="email"
                value={loginData.email}
                onChange={handleLoginChange}
                required
              />
              <label>Contraseña</label>
              <input
                type="password"
                name="password"
                value={loginData.password}
                onChange={handleLoginChange}
                required
              />
              <button type="submit">Entrar</button>
            </form>
            <p>
              ¿No tienes cuenta?{' '}
              <button
                className="link-button"
                onClick={() => setIsLogin(false)}
              >
                Regístrate
              </button>
            </p>
          </>
        ) : (
          <>
            <h2>Registro</h2>
            <form onSubmit={handleRegister}>
              <label>Nombre Completo</label>
              <input
                type="text"
                name="nombre"
                value={registerData.nombre}
                onChange={handleRegisterChange}
                required
              />
              <label>Email</label>
              <input
                type="email"
                name="email"
                value={registerData.email}
                onChange={handleRegisterChange}
                required
              />
              <label>Contraseña</label>
              <input
                type="password"
                name="password"
                value={registerData.password}
                onChange={handleRegisterChange}
                required
              />
              <label>Confirmar Contraseña</label>
              <input
                type="password"
                name="confirmPassword"
                value={registerData.confirmPassword}
                onChange={handleRegisterChange}
                required
              />
              <label>Dirección</label>
              <input
                type="text"
                name="direccion"
                value={registerData.direccion}
                onChange={handleRegisterChange}
                required
              />
              <label>Teléfono</label>
              <input
                type="tel"
                name="telefono"
                value={registerData.telefono}
                onChange={handleRegisterChange}
                required
              />
              <button type="submit">Registrar</button>
            </form>
            <p>
              ¿Ya tienes cuenta?{' '}
              <button
                className="link-button"
                onClick={() => setIsLogin(true)}
              >
                Inicia sesión
              </button>
            </p>
          </>
        )}
      </aside>
    </>
  )
}
