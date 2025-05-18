// src/pages/Register.jsx
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Register() {
  const [email, setEmail]       = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/auth'
    : 'http://localhost:8080/auth'

  async function onSubmit(e) {
    e.preventDefault()
    const res = await fetch(`${apiBase}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
    if (res.ok) {
      alert('Usuario registrado, por favor haz login')
      navigate('/login')
    } else {
      alert('Error al registrar')
    }
  }

  return (
    <main>
      <h1>Registrar</h1>
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
        <button type="submit">Registrar</button>
      </form>
    </main>
  )
}
