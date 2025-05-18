// src/pages/Payment.jsx
import React, { useState } from 'react'

export default function Payment() {
  const [card, setCard] = useState('')
  const apiBase = import.meta.env.PROD
    ? 'https://envejecimiento-exitoso-production.up.railway.app/api'
    : 'http://localhost:8080/api'
  const token = localStorage.getItem('accessToken')

  function onSubmit(e) {
    e.preventDefault()
    fetch(`${apiBase}/pago`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({ tarjeta: card })
    })
    .then(r => {
      if (r.ok) alert('Pago exitoso')
      else throw new Error()
    })
    .catch(() => alert('Error en el pago'))
  }

  return (
    <main>
      <h1>Pagar Pedido</h1>
      <form onSubmit={onSubmit}>
        <label>
          Tarjeta:
          <input
            type="text"
            value={card}
            onChange={e => setCard(e.target.value)}
            required
          />
        </label>
        <button type="submit">Pagar</button>
      </form>
    </main>
  )
}
