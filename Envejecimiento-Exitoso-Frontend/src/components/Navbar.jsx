// src/components/Navbar.jsx
import React, { useContext, useEffect, useRef, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { CartContext }   from "../context/CartContext"
import { AuthContext }   from "../context/AuthContext"
import CartDropdown      from "./CartDropdown"
import "../css/Navbar.css"

export default function Navbar({ onAuthOpen }) {
  /* ------------------------------------------------------------------ */
  const { cart }             = useContext(CartContext)
  const { user, logout }     = useContext(AuthContext)
  const isAdmin              = user?.role === "ADMIN"
  const [showDD, setShowDD]  = useState(false)
  const ddRef                = useRef(null)
  const navigate             = useNavigate()

  /* ------------------------------------------------------------------ */
  useEffect(() => {
    const close = e =>
      ddRef.current && !ddRef.current.contains(e.target) && setShowDD(false)
    document.addEventListener("mousedown", close)
    return () => document.removeEventListener("mousedown", close)
  }, [])

  const totalItems = cart.reduce((s, i) => s + (i.cantidad || 0), 0)
  const handleLogout = () => {
    logout()
    navigate("/")
  }

  /* ------------------------------------------------------------------ */
  return (
    <header className="navbar">
      {/* ---------- IZQUIERDA ---------- */}
      <div className="navbar__left">
        <Link to="/" className="navbar__logo">
          Envejecimiento Exitoso
        </Link>
      </div>

      {/* ---------- CENTRO ---------- */}
      <div className="navbar__center">
        <select className="navbar__category">
          <option>Todos</option><option>Temporada</option><option>Salud</option>
        </select>
        <input className="navbar__search" placeholder="Buscar productos…" />
        <button className="navbar__btn-search">🔍</button>
      </div>

      {/* ---------- DERECHA ---------- */}
      <nav className="navbar__right">
        {user ? (
          <>
            {/* === ADMIN LINKS === */}
            {isAdmin && (
              <>
                <Link to="/admin/productos"        className="navbar__link">
                  Editar productos
                </Link>
                <Link to="/admin/productos/nuevo"  className="navbar__link">
                  Añadir producto
                </Link>
                <Link to="/admin/admins/new"       className="btn-admin">
                Alta administrador
                </Link>
              </>
            )}

            {/* === COMÚN A TODOS LOS LOGUEADOS === */}
            <Link to="/orders" className="navbar__link">Mis Pedidos</Link>
            <button onClick={handleLogout} className="navbar__link logout-button">
              Cerrar sesión
            </button>
          </>
        ) : (
          <span className="navbar__link" style={{ cursor: "pointer" }} onClick={onAuthOpen}>
            Hola, identifícate
          </span>
        )}

        {/* ---------- CARRITO ---------- */}
        <div className="navbar__link navbar__cart" style={{ position:"relative" }}>
          <span onClick={() => setShowDD(o => !o)} style={{ cursor:"pointer" }}>
            🛒 Carrito
            {totalItems > 0 && <span className="cart-badge">{totalItems}</span>}
          </span>

          {showDD && (
            <div ref={ddRef} style={{ position:"absolute", right:0, top:"100%", zIndex:999 }}>
              <CartDropdown />
            </div>
          )}
        </div>
      </nav>
    </header>
  )
}
