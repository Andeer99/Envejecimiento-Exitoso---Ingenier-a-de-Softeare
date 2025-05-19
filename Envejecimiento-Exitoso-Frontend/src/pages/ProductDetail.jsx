// src/pages/ProductDetail.jsx
import React, { useEffect, useState, useContext } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { CartContext } from '../context/CartContext'
import '../css/style.css'
console.log("Hola");

const MOCK_PRODUCTS = [
  {
    id: 1,
    nombreProducto: "Producto 1",
    imagenUrl: "https://picsum.photos/seed/producto1/250/150",
    precioUnitario: 29.99,
    descripcion: "Ejemplo de producto, descripción breve.",
  },
  {
    id: 2,
    nombreProducto: "Producto 2",
    imagenUrl: "https://picsum.photos/seed/producto2/300/250",
    precioUnitario: 39.99,
    descripcion: "Ejemplo de producto, descripción breve.",
  }
]

export default function ProductDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)
  const { addToCart } = useContext(CartContext)
  const [cantidad, setCantidad] = useState(1)
  const [showSnackbar, setShowSnackbar] = useState(false)

  useEffect(() => {
    setLoading(true)
    setTimeout(() => {
      const prod = MOCK_PRODUCTS.find(p => p.id === parseInt(id))
      setProduct(prod || null)
      setLoading(false)
    }, 100)
  }, [id])

  if (loading) return <div className="detail-wrapper"><div>Cargando...</div></div>
  if (!product) return <div className="detail-wrapper"><div>No se encontró el producto.</div></div>

  const handleAddToCart = () => {
    addToCart({ ...product, cantidad })
    setShowSnackbar(true)
    setTimeout(() => setShowSnackbar(false), 1800)
  }

  return (
    <>
      {/* Snackbar animado */}
      {showSnackbar && (
        <div style={{
          position: "fixed",
          bottom: 32,
          left: "50%",
          transform: "translateX(-50%)",
          background: "#28a745",
          color: "#fff",
          padding: "14px 32px",
          borderRadius: 12,
          fontWeight: "bold",
          boxShadow: "0 2px 12px rgba(0,0,0,.18)",
          zIndex: 1000,
          opacity: showSnackbar ? 1 : 0,
          transition: "opacity .3s"
        }}>
          ¡Producto agregado al carrito!
        </div>
      )}
      <div className="detail-wrapper">
        <div className="detail-card">
          <div className="detail-image">
            <img src={product.imagenUrl} alt={product.nombreProducto} />
          </div>
          <div className="detail-info">
            <h2>{product.nombreProducto}</h2>
            <p className="detail-desc">{product.descripcion}</p>
            <div className="detail-prices">
              {product.precioAnterior && (
                <span className="old-price">${product.precioAnterior}</span>
              )}
              <span className="main-price">${product.precioUnitario}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', margin: '10px 0 18px 0' }}>
              <button
                style={{
                  width: 32, height: 32, fontSize: 20, fontWeight: "bold",
                  border: '1px solid #ccc', borderRadius: 4, background: '#f7f7f7', cursor: 'pointer'
                }}
                onClick={() => setCantidad(c => Math.max(1, c - 1))}
              >-</button>
              <input
                type="number"
                min={1}
                value={cantidad}
                onChange={e => setCantidad(Math.max(1, parseInt(e.target.value) || 1))}
                style={{
                  width: 50, textAlign: 'center', margin: '0 8px', fontSize: 18, borderRadius: 4, border: '1px solid #ddd'
                }}
              />
              <button
                style={{
                  width: 32, height: 32, fontSize: 20, fontWeight: "bold",
                  border: '1px solid #ccc', borderRadius: 4, background: '#f7f7f7', cursor: 'pointer'
                }}
                onClick={() => setCantidad(c => c + 1)}
              >+</button>
            </div>
            <button
              className="btn-details"
              onClick={handleAddToCart}
            >
              Agregar al Carrito
            </button>
            <button
              className="btn-secondary"
              style={{ marginLeft: 12 }}
              onClick={() => navigate(-1)}
            >
              Volver
            </button>
          </div>
        </div>
      </div>
    </>
  )
}
