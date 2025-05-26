// src/pages/AddAdmin.jsx
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../css/addAdmin.css'
import { fetchWithAuth } from '../utils/api'

export default function AddAdmin() {
  const [form, setForm] = useState({ nombre:'', email:'', password:'', rfc: '' })
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleChange = e =>
    setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const handleSubmit = async e => {
    e.preventDefault()
    try {
      const token = localStorage.getItem('accessToken')
      const res = await fetchWithAuth(
        `${import.meta.env.VITE_API_URL}/api/usuarios/admin`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({ 
            nombre: form.nombre,
            email: form.email,
            password: form.password,
            rfc: form.rfc,
            role: 'ADMIN'        // <— obligas rol ADMIN
          })
        }
      )
      if (!res.ok) throw new Error('No se pudo crear el administrador')
      navigate('/')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="add-admin">
      <h2>Dar de alta Administrador</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <label>
          Nombre
          <input name="nombre" value={form.nombre} onChange={handleChange} required/>
        </label>
        <label>
          Email
          <input type="email" name="email" value={form.email} onChange={handleChange} required/>
        </label>
        <label>
          Contraseña
          <input type="password" name="password" value={form.password} onChange={handleChange} required/>
        </label>
         <label>
          RFC
          <input
            name="rfc"
            value={form.rfc}
            onChange={handleChange}
            required
            placeholder="EJEMP010101ABC"
          />
        </label>
        <button type="submit">Crear Administrador</button>
      </form>
    </div>
  )
}
