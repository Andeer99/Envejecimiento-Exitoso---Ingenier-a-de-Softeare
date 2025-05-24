// src/pages/ProductDetail.jsx
import React, { useEffect, useState, useContext } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { CartContext } from '../context/CartContext'
import '../css/global.css'

export default function ProductDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { addToCart } = useContext(CartContext)

  const [product, setProduct]   = useState(null)
  const [loading, setLoading]   = useState(true)
  const [error, setError]       = useState(null)
  const [cantidad, setCantidad] = useState(1)
  const [showSnackbar, setShowSnackbar] = useState(false)

  const apiBase = import.meta.env.VITE_API_URL

  useEffect(() => {
    fetch(`${apiBase}/api/productos/${id}`)
      .then(r => r.json())
      .then(data => {
        setProduct(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message || 'Error desconocido')
        setLoading(false)
      })
  }, [id, apiBase])

  const handleAddToCart = () => {
    // ¡Ojo aquí! pasamos el objeto con su cantidad
    addToCart({ ...product, cantidad })
    setShowSnackbar(true)
    setTimeout(() => setShowSnackbar(false), 2000)
  }

  if (loading)  return <main className="main-content"><div>Cargando...</div></main>
  if (error)    return <main className="main-content"><div>Error: {error}</div></main>
  if (!product) return <main className="main-content"><div>No se encontró el producto.</div></main>

  return (
    <main className="main-content">
      {showSnackbar && <div className="snackbar">¡Producto agregado al carrito!</div>}

      <div className="detail-wrapper">
        <div className="detail-card">
          <div className="detail-image">
            <img src={product.imageUrl || '/placeholder.png'} alt={product.nombre} />
          </div>

          <div className="detail-info">
            <h2>{product.nombre}</h2>
            <p className="detail-desc">{product.descripcion}</p>

            <div className="detail-prices">
              {product.precioAnterior && (
                <span className="old-price">${product.precioAnterior.toFixed(2)}</span>
              )}
              <span className="main-price">${product.precio.toFixed(2)}</span>
            </div>

            <div className="quantity-controls">
              <button className="qty-btn" onClick={() => setCantidad(c => Math.max(1, c - 1))}>−</button>
              <input type="text" readOnly className="qty-input" value={cantidad} />
              <button className="qty-btn" onClick={() => setCantidad(c => c + 1)}>+</button>
            </div>

            <div className="action-buttons">
              <button className="btn-details" onClick={handleAddToCart}>
                Agregar al Carrito
              </button>
              <button className="btn-secondary" onClick={() => navigate(-1)}>
                Volver
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  )
}
