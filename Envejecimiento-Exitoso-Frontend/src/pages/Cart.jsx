// src/pages/Cart.jsx
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Cart() {
  const [items, setItems] = useState([])
  const navigate = useNavigate()
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/api'
    : 'http://localhost:8080/api'
  const token = localStorage.getItem('accessToken')

  useEffect(() => {
    fetch(`${apiBase}/carrito`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(r => r.json())
    .then(data => setItems(data.items))
  }, [])

  function remove(id) {
    fetch(`${apiBase}/carrito/items/${id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(() => setItems(items.filter(i => i.id !== id)))
  }

  function checkout() {
    navigate('/confirm')
  }

  return (
    <main>
      <h1>Tu Carrito</h1>
      <ul>
        {items.map(i => (
          <li key={i.id}>
            {i.nombreProducto} x{i.cantidad} — ${i.subtotal.toFixed(2)}
            <button onClick={() => remove(i.id)}>Eliminar</button>
          </li>
        ))}
      </ul>
      <button onClick={checkout}>Confirmar Pedido</button>
    </main>
  )
}
