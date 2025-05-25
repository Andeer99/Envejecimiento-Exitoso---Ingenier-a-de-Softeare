// src/pages/Catalog.jsx
import React, { useContext, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { ProductsContext } from "../context/ProductsContext";
import { resolveImg } from "../utils/resolveImg";
import "../css/global.css";

export default function Catalog() {
  /* contexto: caemos en [] si todavía no llega la data */
  const { products = [] } = useContext(ProductsContext);

  /* búsqueda */
  const [query, setQuery] = useState("");

  const filtered = useMemo(() => {
    const q = query.toLowerCase();
    return products.filter(p =>
      (p.nombre || p.nombreProducto || "").toLowerCase().includes(q)
    );
  }, [products, query]);

  /* render */
  return (
    <main className="main-content">
      <h1>Catálogo de Productos</h1>
      <section className="product-grid">
        {filtered.map(p => {
          const imgSrc = resolveImg(p.imageUrl);
          const name   = p.nombre || p.nombreProducto || "Producto";
          const price  = Number(p.precio ?? p.precioUnitario ?? 0);

          return (
            <div className="product-card" key={p.id}>
              <img
                src={imgSrc}
                alt={name}
                onError={e => {
                  e.currentTarget.onerror = null;
                  e.currentTarget.src = "/placeholder.png";
                }}
              />

              <h2>{name}</h2>
              <div className="price">${price.toFixed(2)}</div>
              <Link to={`/producto/${p.id}`}>Ver detalle</Link>
            </div>
          );
        })}
      </section>
    </main>
  );
}
