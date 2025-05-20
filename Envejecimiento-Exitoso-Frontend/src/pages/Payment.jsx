import React, { useContext, useState } from 'react'
import { CartContext }             from '../context/CartContext'
import { useNavigate }             from 'react-router-dom'

export default function Payment() {
  const { cart, clearCart } = useContext(CartContext)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const apiBase = import.meta.env.VITE_API_URL
  const token   = localStorage.getItem('accessToken')

  const total = cart.reduce(
    (sum, p) => sum + (p.cantidad||0)*(p.precioUnitario||0),
    0
  )

  // 1️⃣ Crea el pedido y devuelve su id
  async function crearPedido() {
    const res = await fetch(`${apiBase}/pedidos`, {
      method: "POST",
      headers: {
        "Content-Type":  "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify({ items: cart })
    })
    if (!res.ok) throw new Error("Error creando pedido")
    const data = await res.json()
    return data.id
  }

  // 2️⃣ Inicia el pago para ese pedido
  async function iniciarPago(pedidoId) {
    const res = await fetch(
      `${apiBase}/pagos/iniciar?pedidoId=${pedidoId}`, {
        method: "POST",
        headers: { "Authorization": `Bearer ${token}` }
      }
    )
    if (!res.ok) throw new Error("Error iniciando pago")
    return await res.json()
  }

  // 3️⃣ Función que orquesta todo
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

  return (
    <div style={{ maxWidth:600, margin:"56px auto", padding:30 }}>
      <h2>Resumen del Pedido</h2>
      <p>Total: <strong>${total.toFixed(2)}</strong></p>
      <button
        onClick={confirmarPago}
        disabled={loading}
        style={{
          width:"100%", padding:12, fontSize:16,
          background:"#28a745", color:"#fff", border:"none", borderRadius:4,
          cursor: loading ? "wait" : "pointer"
        }}
      >
        {loading ? "Procesando…" : "Confirmar Pago"}
      </button>
    </div>
  )
}
