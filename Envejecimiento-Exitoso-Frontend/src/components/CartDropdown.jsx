// src/components/CartDropdown.jsx
import React, { useContext } from 'react'
import { CartContext } from '../context/CartContext'
import { Link } from 'react-router-dom'

export default function CartDropdown() {
  const { cart } = useContext(CartContext)

  if (!cart || cart.length === 0) {
    return (
      <div style={{
        padding: '1rem',
        minWidth: 220,
        color: "#333",
        textAlign: "center"
      }}>
        Tu carrito está vacío
      </div>
    )
  }

  const total = cart.reduce((sum, p) => sum + ((p.cantidad || 0) * (p.precioUnitario || 0)), 0)

  return (
    <div style={{
      minWidth: 240,
      padding: 14,
      background: "#fff",
      borderRadius: 8,
      boxShadow: "0 2px 10px rgba(0,0,0,0.09)",
      border: "1px solid #e5e5e5"
    }}>
      <ul style={{ listStyle: "none", margin: 0, padding: 0 }}>
        {cart.map(item => (
          <li key={item.id} style={{
            marginBottom: 8,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            fontSize: "1em"
          }}>
            <span style={{ color: "#222", fontWeight: 500, maxWidth: 110, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>
              {item.nombreProducto} <span style={{ color: "#317ada" }}>x{item.cantidad || 0}</span>
            </span>
            <span style={{
              fontWeight: "bold",
              color: "#2c3e50",
              minWidth: 64,
              textAlign: "right"
            }}>
              ${((item.precioUnitario || 0) * (item.cantidad || 0)).toLocaleString("es-MX", { minimumFractionDigits: 2 })}
            </span>
          </li>
        ))}
      </ul>
      <div style={{
        marginTop: 12,
        fontWeight: "bold",
        color: "#317ada",
        borderTop: "1px solid #eee",
        paddingTop: 8,
        fontSize: "1.08em"
      }}>
        Total: ${total.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
      </div>
      <Link to="/cart">
        <button style={{
          width: "100%",
          marginTop: 14,
          background: "#317ada",
          color: "#fff",
          border: "none",
          borderRadius: 4,
          padding: "10px 0",
          fontWeight: 600,
          fontSize: "1em",
          cursor: "pointer"
        }}>
          Ir al carrito
        </button>
      </Link>
    </div>
  )
}
