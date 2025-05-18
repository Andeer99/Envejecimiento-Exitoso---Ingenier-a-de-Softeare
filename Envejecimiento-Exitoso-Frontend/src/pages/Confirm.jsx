// src/pages/Confirm.jsx
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Confirm() {
  const [order, setOrder] = useState(null)
  const navigate = useNavigate()
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/api'
    : 'http://localhost:8080/api'
  const token = localStorage.getItem('accessToken')

  useEffect(() => {
    // asumir que el backend crea el pedido al hacer confirm
    fetch(`${apiBase}/pedido`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      }
    })
    .then(r => r.json())
    .then(setOrder)
  }, [])

  if (!order) return <p>Cargando...</p>

  return (
    <main>
      <h1>Pedido Creado</h1>
      <p>ID: {order.id}</p>
      <p>Estado: {order.estado}</p>
      <button onClick={() => navigate('/payment')}>Pagar</button>
    </main>
  )
}
