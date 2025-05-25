// src/pages/ProductDetail.jsx
import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";

import { CartContext } from "../context/CartContext";
import { resolveImg }  from "../utils/resolveImg";

import "../css/global.css";

export default function ProductDetail() {
  const { id }      = useParams();
  const navigate    = useNavigate();
  const { addToCart } = useContext(CartContext);

  const [product,   setProduct]   = useState(null);
  const [loading,   setLoading]   = useState(true);
  const [error,     setError]     = useState(null);
  const [cantidad,  setCantidad]  = useState(1);
  const [snack,     setSnack]     = useState(false);

  const apiBase = import.meta.env.VITE_API_URL;

  /* ------------ carga del producto ------------- */
  useEffect(() => {
    fetch(`${apiBase}/api/productos/${id}`)
      .then(r => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        return r.json();
      })
      .then(data => { setProduct(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, [id, apiBase]);

  /* ------------ handlers ------------- */
  const handleAdd = () => {
    addToCart({ ...product, cantidad });
    setSnack(true);
    setTimeout(() => setSnack(false), 2000);
  };

  /* ------------ estados de espera ------------- */
  if (loading)  return <main className="main-content">Cargando…</main>;
  if (error)    return <main className="main-content">Error: {error}</main>;
  if (!product) return <main className="main-content">Producto no encontrado</main>;

  const imgSrc = resolveImg(product.imageUrl);   // ← clave correcta

  /* ------------ render ------------- */
  return (
    <main className="main-content">
      {snack && <div className="snackbar">¡Producto agregado al carrito!</div>}

      <div className="detail-wrapper">
        <div className="detail-card">
          <div className="detail-image">
            <img
              src={imgSrc}
              alt={product.nombre}
              onError={e => {
                e.currentTarget.onerror = null;
                e.currentTarget.src = "/placeholder.png";
              }}
            />
          </div>

          <div className="detail-info">
            <h2>{product.nombre}</h2>
            <p className="detail-desc">{product.descripcion}</p>

            <div className="detail-prices">
              {product.precioAnterior && (
                <span className="old-price">
                  ${product.precioAnterior.toFixed(2)}
                </span>
              )}
              <span className="main-price">${product.precio.toFixed(2)}</span>
            </div>

            <div className="quantity-controls">
              <button onClick={() => setCantidad(c => Math.max(1, c - 1))} className="qty-btn">−</button>
              <input readOnly value={cantidad} className="qty-input" />
              <button onClick={() => setCantidad(c => c + 1)} className="qty-btn">+</button>
            </div>

            <div className="action-buttons">
              <button className="btn-details" onClick={handleAdd}>
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
  );
}
