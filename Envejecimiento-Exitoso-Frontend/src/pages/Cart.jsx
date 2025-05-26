import React, { useContext } from 'react'
import { CartContext } from '../context/CartContext'
import { useNavigate } from 'react-router-dom'
import {resolveImg} from "../utils/resolveImg";

export default function Cart() {
  const { cart, removeFromCart, updateQuantity } = useContext(CartContext)
  const navigate = useNavigate()

  const total = cart.reduce((sum, p) => sum + (p.cantidad || 0) * (p.precio  || 0),  0 )

  function handleCantidadChange(id, cantidad) {
    if (cantidad < 1) return
    updateQuantity(id, cantidad)
  }

  if (!cart.length) return <div style={{ padding: 32, textAlign: "center" }}>Tu carrito está vacío.</div>

  return (
    <div className="cart-page" style={{ maxWidth: 780, margin: "36px auto", background: "#fff", borderRadius: 12, boxShadow: "0 2px 12px #0001", padding: 24 }}>
      <table style={{ width: "100%", borderCollapse: "collapse", marginBottom: 18 }}>
        <thead>
          <tr style={{ background: "#f5f5f5", textAlign: "left" }}>
            <th>Producto</th>
            <th>Nombre</th>
            <th>Cantidad</th>
            <th>Precio Unitario</th>
            <th>Subtotal</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {cart.map(item => (
            <tr key={item.id}>
              <td><img src={resolveImg(item.imageUrl)} alt={item.nombre} width={48} /></td>
              <td>{item.nombre}</td>
              <td>
                <input
                  type="number"
                  min={1}
                  value={item.cantidad}
                  onChange={e => handleCantidadChange(item.id, parseInt(e.target.value) || 1)}
                  style={{ width: 48, textAlign: "center" }}
                />
              </td>
              <td>${(item.precio || 0).toFixed(2)}</td>
              <td>${((item.precio || 0) * (item.cantidad || 0)).toFixed(2)}</td>
              <td>
                <button
                  style={{
                    background: "#ee433c", color: "#fff", border: "none", borderRadius: 4, padding: "6px 14px", cursor: "pointer"
                  }}
                  onClick={() => removeFromCart(item.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div style={{ textAlign: "right", marginTop: 16 }}>
        <div style={{ fontSize: 20, marginBottom: 10 }}>
          <strong>Resumen</strong><br />
          Total: <span style={{ fontWeight: 700, color: "#228b22" }}>${total.toFixed(2)}</span>
        </div>
        <button
          style={{
            background: "#28a745", color: "#fff", fontWeight: 600, border: "none",
            borderRadius: 4, padding: "12px 30px", fontSize: 17, cursor: "pointer"
          }}
          onClick={() => navigate("/payment")}
        >
          Proceder a Pagar
        </button>
      </div>
    </div>
  )
}
