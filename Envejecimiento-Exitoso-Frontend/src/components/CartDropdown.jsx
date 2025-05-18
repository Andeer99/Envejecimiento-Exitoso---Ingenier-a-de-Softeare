// src/components/CartDropdown.jsx
import React, { useEffect, useState } from 'react'
export default function CartDropdown() {
  const [items, setItems] = useState([])
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/api'
    : 'http://localhost:8080/api'

  useEffect(() => {
    const token = localStorage.getItem('accessToken')
    fetch(`${apiBase}/carrito`, {
      headers: { Authorization: token ? `Bearer ${token}` : '' }
    })
      .then(r => r.json())
      .then(data => setItems(data.items || []))
      .catch(() => setItems([]))
  }, [])

  return (
    <div className="cart-dropdown">
      {items.length === 0
        ? <p style={{ padding: '1rem' }}>Tu carrito está vacío</p>
        : (
          <ul>
            {items.map(i => (
              <li key={i.id}>
                {i.nombreProducto} x{i.cantidad}
                <strong>${(i.subtotal).toFixed(2)}</strong>
              </li>
            ))}
          </ul>
        )
      }
    </div>
  )
}
