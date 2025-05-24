// src/pages/Catalog.jsx
import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import '../css/global.css'            // importa estilos globales

export default function Catalog () {
  const [items, setItems] = useState([])

  useEffect(() => {
    const apiBase = import.meta.env.VITE_API_URL
    fetch(`${apiBase}/api/productos`)
      .then(r => r.json())
      .then(data => {
        setItems(data)
      })
      .catch(console.error)
  }, [])

  return (
    <main className="main-content">
      <h1>Catálogo de Productos</h1>

      <section className="product-grid">
        {items.map(p => {
          const img   = p.imageUrl      || p.imagenUrl      || '/placeholder.png'
          const name  = p.nombre        || p.nombreProducto || 'Producto'
          const price = p.precio ?? p.precioUnitario ?? 0
          return (
            <div className="product-card" key={p.id}>
              <img src={img} alt={name} />
              <h2>{name}</h2>
              <div className="price">${price.toFixed(2)}</div>
              <Link to={`/producto/${p.id}`}>Ver detalle</Link>
            </div>
          )
        })}
      </section>
    </main>
  )
}
