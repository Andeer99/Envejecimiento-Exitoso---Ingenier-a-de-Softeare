import React, { useEffect, useState } from 'react'
import '../css/style.css'
import '../css/pagination.css'
import { Link } from 'react-router-dom'

export default function Home() {
  const [products, setProducts] = useState([])
  const [query, setQuery] = useState('')
  const [page, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)

  const apiBase = import.meta.env.VITE_API_URL;
  useEffect(() => {
    fetch(`${apiBase}/api/productos?page=${page}&size=6`)
      .then(res => res.json())
      .then(data => {
        setProducts(data.content || data)
        setTotalPages(data.totalPages || 1)
      })
      .catch(() => {
        setProducts([])
        setTotalPages(1)
      })
  }, [page])

  const filtered = products.filter(p =>
    p.nombreProducto?.toLowerCase().includes(query.toLowerCase())
  )

  return (
    <div className="home-wrapper">
      <h1 className="home-title">Productos de Temporada</h1>
      <p className="home-desc">Encuentra los mejores artículos para cada estación</p>
      
      <section className="product-grid">
        {filtered.length === 0 && (
          <div style={{ gridColumn: "1/-1", textAlign: "center" }}>
            No se encontraron productos.
          </div>
        )}
        {filtered.map(p => (
          <div className="product-card" key={p.id}>
            <img src={p.imagenUrl} alt={p.nombreProducto} />
            <h3>{p.nombreProducto}</h3>
            <p style={{ color: "#ee433c", fontWeight: "bold", margin: 0 }}>${p.precioUnitario}</p>
            <Link to={`/producto/${p.id}`} style={{ textDecoration: "none", width: "100%" }}>
              <button className="btn-details">
                Ver Detalles
              </button>
            </Link>
          </div>
        ))}
      </section>
      {totalPages > 1 && (
        <div className="pagination">
          <button onClick={() => setPage(p => Math.max(p - 1, 1))} disabled={page === 1}>
            « Anterior
          </button>
          {[...Array(totalPages)].map((_, i) => (
            <button
              key={i}
              onClick={() => setPage(i + 1)}
              className={page === i + 1 ? 'active' : ''}
            >
              {i + 1}
            </button>
          ))}
          <button onClick={() => setPage(p => Math.min(p + 1, totalPages))} disabled={page === totalPages}>
            Siguiente »
          </button>
        </div>
      )}
    </div>
  )
}
