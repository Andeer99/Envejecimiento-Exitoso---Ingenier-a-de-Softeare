// src/components/LoginModal.jsx
import React, { useState } from 'react'

export default function LoginModal({ onClose }) {
  const [email, setEmail]       = useState('')
  const [password, setPassword] = useState('')
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/auth'
    : 'http://localhost:8080/auth'

  function handleLogin(e) {
    e.preventDefault()
    fetch(`${apiBase}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
      .then(r => r.json())
      .then(({ accessToken, refreshToken }) => {
        localStorage.setItem('accessToken', accessToken)
        localStorage.setItem('refreshToken', refreshToken)
        onClose()
      })
      .catch(() => alert('Credenciales inválidas'))
  }

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <form className="modal" onClick={e => e.stopPropagation()} onSubmit={handleLogin}>
        <h2>Iniciar Sesión</h2>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
        <button type="submit">Entrar</button>
      </form>
    </div>
  )
}
