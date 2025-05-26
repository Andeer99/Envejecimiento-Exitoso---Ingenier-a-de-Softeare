// src/pages/Payment.jsx
import React, { useContext, useState } from 'react'
import { useNavigate }      from 'react-router-dom'
import { CartContext }      from '../context/CartContext'
import { resolveImg }       from '../utils/resolveImg'
import { fetchAuth }        from '../utils/fetchAuth'    // ⬅️ usa wrapper

export default function Payment () {
  const { cart, clearCart } = useContext(CartContext)
  const [loading, setLoading] = useState(false)
  const navigate  = useNavigate()
  const apiBase   = import.meta.env.VITE_API_URL

  /* ------------------------ helpers ------------------------ */
  /** Convierte el carrito a la forma { productoId, cantidad } */
  const buildItemsDto = () =>
    cart.map(({ id, cantidad }) => ({
      productoId: id,
      cantidad
    }))

  /** POST /api/pedidos  ->  devuelve id del pedido */
  const crearPedido = async () => {
    const res = await fetchAuth(`${apiBase}/api/pedidos`, {
      method : 'POST',
      body   : JSON.stringify({ items: buildItemsDto() })
    })
    if (!res.ok) throw new Error(await res.text())
    return (await res.json()).id
  }

  /** POST /api/pagos/iniciar?pedidoId=X -> inicia pasarela */
  const iniciarPago = pedidoId =>
    fetchAuth(`${apiBase}/api/pagos/iniciar?pedidoId=${pedidoId}`, {
      method: 'POST'
    })

  /* --------------------- handler principal ------------------ */
  const confirmarPago = async e => {
    e.preventDefault()
    setLoading(true)
    try {
      const pedidoId = await crearPedido()
      await iniciarPago(pedidoId)
      clearCart()
      navigate('/confirm')
    } catch (err) {
      console.error(err)
      alert(err.message || 'No se pudo completar el pago')
    } finally {
      setLoading(false)
    }
  }

  /* ------------------------ UI ------------------------ */
  const total = cart.reduce(
    (sum, p) => sum + (p.cantidad || 0) * (p.precio || 0),
    0
  )

  if (!cart.length) {
    return (
      <div style={{ padding: 32, textAlign: 'center' }}>
        Tu carrito está vacío.
      </div>
    )
  }

  return (
    <div className="payment-container">
      <h2>Resumen del Pedido</h2>

      <div className="payment-items">
        {cart.map(it => (
          <div key={it.id} className="payment-item">
            <img
              src={resolveImg(it.imageUrl)}
              alt={it.nombre}
              onError={e => {
                e.currentTarget.onerror = null
                e.currentTarget.src = '/placeholder.png'
              }}
            />
            <div className="item-info">
              <p className="item-name">{it.nombre}</p>
              <p className="item-qty">Cantidad: {it.cantidad}</p>
              <p className="item-sub">
                Subtotal: ${(it.precio * it.cantidad).toFixed(2)}
              </p>
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
        {loading ? 'Procesando…' : 'Confirmar Pago'}
      </button>
    </div>
  )
}
