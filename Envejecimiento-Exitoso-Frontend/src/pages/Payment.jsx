import React, { useContext, useState } from 'react'
import { CartContext } from '../context/CartContext'
import { useNavigate } from 'react-router-dom'
import { resolveImg } from '../utils/resolveImg'

export default function Payment() {
  const { cart, clearCart } = useContext(CartContext)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const apiBase = import.meta.env.VITE_API_URL
  const token   = localStorage.getItem('accessToken')

  const total = cart.reduce(
    (sum, p) => sum + (p.cantidad||0)*(p.precio||0),
    0
  )

  async function crearPedido() {
    const res = await fetch(`${apiBase}/api/pedidos`, {
      method: "POST",
      headers: {
        "Content-Type":  "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify({ items: cart })
    })
    if (!res.ok) throw new Error("Error creando pedido")
    return (await res.json()).id
  }

  async function iniciarPago(pedidoId) {
    const res = await fetch(
      `${apiBase}/api/pagos/iniciar?pedidoId=${pedidoId}`, {
        method: "POST",
        headers: { "Authorization": `Bearer ${token}` }
      }
    )
    if (!res.ok) throw new Error("Error iniciando pago")
    return await res.json()
  }

  async function confirmarPago(e) {
    e.preventDefault()
    setLoading(true)
    try {
      const pedidoId = await crearPedido()
      await iniciarPago(pedidoId)
      clearCart()
      navigate("/confirm")
    } catch (err) {
      console.error(err)
      alert(err.message)
    } finally {
      setLoading(false)
    }
  }

  if (!cart.length) {
    return <div style={{ padding: 32, textAlign: "center" }}>
      Tu carrito está vacío.
    </div>
  }

  return (
    <div className="payment-container">
      <h2>Resumen del Pedido</h2>

      <div className="payment-items">
        {cart.map(item => (
          <div key={item.id} className="payment-item">
            <img
              src={resolveImg(item.imageUrl)}
              alt={item.nombre}
              onError={e => { e.currentTarget.onerror = null; e.currentTarget.src = "/placeholder.png" }}
            />
            <div className="item-info">
              <p className="item-name">{item.nombre}</p>
              <p className="item-qty">Cantidad: {item.cantidad}</p>
              <p className="item-sub">Subtotal: ${(item.precio * item.cantidad).toFixed(2)}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="summary-total">
        Total: <strong>${total.toFixed(2)}</strong>
      </div>

      <button
        className="confirm-btn"
        onClick={confirmarPago}
        disabled={loading}
      >
        {loading ? "Procesando…" : "Confirmar Pago"}
      </button>
    </div>
  )
}
