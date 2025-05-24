// src/pages/Home.jsx
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import '../css/global.css'          // ruta correcta
import '../css/pagination.css'

export default function Home () {
  const [products, setProducts]     = useState([])
  const [query, setQuery]           = useState('')
  const [page, setPage]             = useState(1)
  const [totalPages, setTotalPages] = useState(1)

  const apiBase = import.meta.env.VITE_API_URL

  /* ---------- GET paginado ---------- */
  useEffect(() => {
    fetch(`${apiBase}/api/productos?page=${page}&size=6`)
      .then(r => r.json())
      .then(data => {
        setProducts(data.content ?? data)
        setTotalPages(data.totalPages ?? 1)
      })
      .catch(() => {
        setProducts([])
        setTotalPages(1)
      })
  }, [page, apiBase])

  /* ---------- filtro local ---------- */
  const filtered = products.filter(p =>
    (p.nombre || p.nombreProducto || '')
      .toLowerCase()
      .includes(query.toLowerCase())
  )

  return (
    <main className="main-content">
      <h1>Productos de Temporada</h1>
      <p className="home-desc">
        Encuentra los mejores artículos para cada estación
      </p>

      {/* búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar producto…"
          value={query}
          onChange={e => setQuery(e.target.value)}
        />
        <button>🔍</button>
      </div>

      {/* grid */}
      <section className="product-grid">
        {filtered.length === 0 && (
          <div style={{ gridColumn: '1/-1', textAlign: 'center' }}>
            No se encontraron productos.
          </div>
        )}

        {filtered.map(p => {
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

      {/* paginación */}
      {totalPages > 1 && (
        <ul className="pagination">
          <li
            className={page === 1 ? 'disabled' : ''}
            onClick={() => setPage(p => Math.max(p - 1, 1))}
          >
            « Anterior
          </li>

          {Array.from({ length: totalPages }, (_, i) => i + 1).map(n => (
            <li
              key={n}
              className={page === n ? 'active' : ''}
              onClick={() => setPage(n)}
            >
              {n}
            </li>
          ))}

          <li
            className={page === totalPages ? 'disabled' : ''}
            onClick={() => setPage(p => Math.min(p + 1, totalPages))}
          >
            Siguiente »
          </li>
        </ul>
      )}
    </main>
  )
}
