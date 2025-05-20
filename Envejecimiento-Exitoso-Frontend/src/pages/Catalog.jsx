// src/pages/Catalog.jsx
import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

export default function Catalog() {
  const [items, setItems] = useState([])

  useEffect(() => {
    const apiBase = import.meta.env.VITE_API_URL;
    fetch(`${apiBase}/productos`)
      .then(r => r.json())
      .then(setItems)
      .catch(console.error)
  }, [])

  return (
    <main>
      <h1>Catálogo de Productos</h1>
      <section className="product-grid">
        {items.map(p => (
          <div className="product-card" key={p.id}>
            <img src={p.imagenUrl} alt={p.nombreProducto} />
            <h2>{p.nombreProducto}</h2>
            <div>${p.precioUnitario.toFixed(2)}</div>
            <Link to={`/product/${p.id}`}>Ver detalle</Link>
          </div>
        ))}
      </section>
    </main>
  )
}
