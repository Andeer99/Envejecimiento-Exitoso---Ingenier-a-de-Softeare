// src/components/Navbar.jsx
import React from 'react'
import { Link } from 'react-router-dom'
import '../css/Navbar.css'

export default function Navbar() {
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
        <Link to="/login"    className="navbar__link">Hola, Identifícate</Link>
        <Link to="/orders"   className="navbar__link">Devoluciones y Pedidos</Link>
        <Link to="/cart"     className="navbar__link navbar__cart">Carrito</Link>
      </nav>
    </header>
)
}
