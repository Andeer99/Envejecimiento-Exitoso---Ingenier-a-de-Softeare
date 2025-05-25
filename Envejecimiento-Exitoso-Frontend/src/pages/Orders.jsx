// src/pages/Orders.jsx
import React, { useState, useEffect } from "react";
import { fetchAuth } from "../utils/fetchAuth";

export default function Orders() {
  const [orders, setOrders] = useState([]);            // ← ahora sí importado
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState(null);

  useEffect(() => {
    setLoading(true);
    fetchAuth(`${import.meta.env.VITE_API_URL}/pedidos`)
      .then((res) => {
        if (!res.ok) throw new Error("No se pudieron cargar los pedidos");
        return res.json();
      })
      .then((data) => setOrders(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div style={{ padding: 32, textAlign: "center" }}>Cargando pedidos…</div>;
  if (error)   return <div style={{ padding: 32, textAlign: "center", color: "red" }}>Error: {error}</div>;
  if (orders.length === 0) {
    return <div style={{ padding: 32, textAlign: "center" }}>Aún no tienes pedidos realizados.</div>;
  }

  return (
    <div style={{ maxWidth: 650, margin: "48px auto", padding: "24px 10px" }}>
      <h2 style={{ fontWeight: 800, marginBottom: 28, textAlign: "center" }}>Historial de Pedidos</h2>
      {orders.map((pedido) => (
        <div key={pedido.id} style={{ marginBottom: 28, padding: "20px 26px", background: "#fff", borderRadius: 11, boxShadow: "0 2px 12px #0001" }}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 8 }}>
            <span style={{ fontWeight: 700, color: "#234ca4" }}>Pedido {pedido.id}</span>
            <span style={{ color: "#21a03c", fontWeight: 700 }}>{pedido.estado}</span>
          </div>
          <div style={{ fontSize: 15, marginBottom: 9, color: "#888" }}>Fecha: {pedido.fecha}</div>
          <table style={{ width: "100%", fontSize: 15, marginBottom: 12 }}>
            <thead>
              <tr style={{ background: "#f8fafb" }}>
                <th style={{ textAlign: "left", padding: "6px" }}>Producto</th>
                <th style={{ textAlign: "center", padding: "6px" }}>Cantidad</th>
                <th style={{ textAlign: "center", padding: "6px" }}>Precio</th>
              </tr>
            </thead>
            <tbody>
              {pedido.productos.map((prod, idx) => (
                <tr key={idx} style={{ background: "#fafdff" }}>
                  <td style={{ padding: "6px" }}>{prod.nombre}</td>
                  <td style={{ textAlign: "center", padding: "6px" }}>{prod.cantidad}</td>
                  <td style={{ textAlign: "center", padding: "6px" }}>
                    ${prod.precio.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div style={{ textAlign: "right", fontWeight: 700, color: "#2b8246", fontSize: 16 }}>
            Total: ${pedido.total.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
          </div>
        </div>
      ))}
    </div>
  );
}
