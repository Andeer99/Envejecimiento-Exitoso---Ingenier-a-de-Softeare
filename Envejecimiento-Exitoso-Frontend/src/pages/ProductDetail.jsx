// src/pages/ProductDetail.jsx
import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CartContext } from '../context/CartContext';
import '../css/style.css';

export default function ProductDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addToCart } = useContext(CartContext);

  const [product, setProduct] = useState(null);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState(null);
  const [cantidad, setCantidad] = useState(1);
  const [showSnackbar, setShowSnackbar] = useState(false);

  // Base URL de tu API vendrá de .env: VITE_API_URL
  const apiBase = import.meta.env.VITE_API_URL;

  useEffect(() => {
    setLoading(true);
    fetch(`${apiBase}/api/productos/${id}`)
      .then(res => {
        if (!res.ok) throw new Error(`Producto ${id} no encontrado`);
        return res.json();
      })
      .then(data => {
        setProduct(data);
        setError(null);
      })
      .catch(err => {
        console.error(err);
        setError(err.message);
        setProduct(null);
      })
      .finally(() => setLoading(false));
  }, [id, apiBase]);

  // Handler para añadir al carrito
  const handleAddToCart = () => {
    addToCart({ ...product, cantidad });
    setShowSnackbar(true);
    setTimeout(() => setShowSnackbar(false), 1800);
  };

  // Estados de loading / error / no hay producto
  if (loading) {
    return <div className="detail-wrapper"><div>Cargando...</div></div>;
  }
  if (error) {
    return <div className="detail-wrapper"><div>Error: {error}</div></div>;
  }
  if (!product) {
    return <div className="detail-wrapper"><div>No se encontró el producto.</div></div>;
  }

  // Renderizado principal
  return (
    <>
      {/* Snackbar */}
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

            {/* Cantidad */}
            <div style={{ display: 'flex', alignItems: 'center', margin: '10px 0 18px 0' }}>
              <button
                style={quantityBtnStyle}
                onClick={() => setCantidad(c => Math.max(1, c - 1))}
              >−</button>
              <input
                type="number"
                min={1}
                value={cantidad}
                onChange={e => setCantidad(Math.max(1, parseInt(e.target.value)||1))}
                style={quantityInputStyle}
              />
              <button
                style={quantityBtnStyle}
                onClick={() => setCantidad(c => c + 1)}
              >+</button>
            </div>

            {/* Botones */}
            <div style={{ display: 'flex', gap: 12 }}>
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
    </>
  );
}

// Estilos en línea reutilizables
const quantityBtnStyle = {
  width: 32, height: 32, fontSize: 20, fontWeight: "bold",
  border: '1px solid #ccc', borderRadius: 4, background: '#f7f7f7', cursor: 'pointer'
};
const quantityInputStyle = {
  width: 50, textAlign: 'center', margin: '0 8px',
  fontSize: 18, borderRadius: 4, border: '1px solid #ddd'
};
