// src/components/Navbar.jsx
import React, { useContext, useState, useRef, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { CartContext } from '../context/CartContext'
import CartDropdown from './CartDropdown'
import '../css/Navbar.css'

export default function Navbar({ onAuthOpen }) {
  const { cart } = useContext(CartContext)
  console.log("cart en navbar", cart);
  
  const [showDropdown, setShowDropdown] = useState(false)
  const dropdownRef = useRef(null)

  // Maneja click fuera del dropdown para cerrarlo
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  // Suma total de productos en el carrito (ejemplo: 2x item1 + 1x item2 = 3)
const totalItems = cart.reduce((sum, item) => sum + (item.cantidad || 0), 0)

  return (
    <header className="navbar">
      <div className="navbar__left">
        <Link to="/" className="navbar__logo">Envejecimiento Exitoso</Link>
      </div>

      <div className="navbar__center">
        <select className="navbar__category">
          <option>Todos</option>
          <option>Temporada</option>
          <option>Salud</option>
        </select>
        <input
          type="text"
          className="navbar__search"
          placeholder="Buscar productos, marcas y más…"
        />
        <button className="navbar__btn-search">🔍</button>
      </div>

      <nav className="navbar__right">
        <span
        className="navbar__link"
        style={{ cursor: 'pointer' }}
        onClick={onAuthOpen}
      >Hola, Identifícate
      </span>

        <Link to="/orders" className="navbar__link">Mis Pedidos</Link>
        {/* Carrito con badge y dropdown */}
        <div className="navbar__link navbar__cart" style={{ position: "relative" }}>
          <span
            onClick={() => setShowDropdown((prev) => !prev)}
            style={{ cursor: 'pointer', position: "relative" }}
          >
            🛒 Carrito
            {totalItems > 0 && (
              <span className="cart-badge">{totalItems}</span>
            )}
          </span>
          {/* Dropdown */}
          {showDropdown && (
            <div
              ref={dropdownRef}
              style={{
                position: "absolute",
                right: 0,
                top: "100%",
                zIndex: 999,
                background: "#fff",
                boxShadow: "0 2px 8px rgba(0,0,0,.15)"
              }}
            >
              <CartDropdown />
            </div>
          )}
        </div>
      </nav>
    </header>
  )
}
