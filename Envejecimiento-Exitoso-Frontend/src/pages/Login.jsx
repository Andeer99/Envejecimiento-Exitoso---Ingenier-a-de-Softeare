// src/pages/Login.jsx
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const [email, setEmail]     = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/auth'
    : 'http://localhost:8080/auth'

  async function onSubmit(e) {
    e.preventDefault()
    const res = await fetch(`${apiBase}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
    if (res.ok) {
      const { accessToken, refreshToken } = await res.json()
      localStorage.setItem('accessToken', accessToken)
      localStorage.setItem('refreshToken', refreshToken)
      navigate('/catalog')
    } else {
      alert('Credenciales inválidas')
    }
  }

  return (
    <main>
      <h1>Iniciar Sesión</h1>
      <form onSubmit={onSubmit}>
        <label>
          Email:
          <input 
            type="email" 
            value={email}
            onChange={e => setEmail(e.target.value)}
            required />
        </label>
        <label>
          Contraseña:
          <input 
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required />
        </label>
        <button type="submit">Login</button>
      </form>
    </main>
  )
}
