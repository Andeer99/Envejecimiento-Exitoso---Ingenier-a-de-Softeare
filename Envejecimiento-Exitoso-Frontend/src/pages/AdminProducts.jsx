// src/pages/AdminProducts.jsx
import React, { useState, useEffect, useContext } from "react"
import { AuthContext } from "../context/AuthContext"
import { useNavigate } from "react-router-dom"
import "../css/global.css"

export default function AdminProducts() {
  const { user } = useContext(AuthContext)
  const navigate  = useNavigate()

  const [items, setItems]   = useState([])
  const [editing, setEditing] = useState(false)
  const [dirty, setDirty]     = useState(false)

  const apiBase = import.meta.env.VITE_API_URL

  /* -------- carga inicial -------- */
  useEffect(() => {
    if (user?.role !== "ADMIN") return navigate("/")
    fetch(`${apiBase}/api/productos`)
      .then(r => r.json())
      .then(setItems)
      .catch(console.error)
  }, [user, apiBase, navigate])

  /* -------- handlers -------- */
  const handleChange = (id, field, value) => {
    setDirty(true)
    setItems(prev =>
      prev.map(p => (p.id === id ? { ...p, [field]: value } : p))
    )
  }

  const handleDelete = id => {
    if (!window.confirm("¿Eliminar producto?")) return
    fetch(`${apiBase}/api/productos/${id}`, { method:"DELETE" })
      .then(() => setItems(prev => prev.filter(p => p.id !== id)))
      .catch(console.error)
  }

  const handleSave = () => {
    Promise.all(
      items.map(p =>
        fetch(`${apiBase}/api/productos/${p.id}`, {
          method:"PUT",
          headers:{ "Content-Type":"application/json" },
          body:JSON.stringify(p)
        })
      )
    )
      .then(() => { setEditing(false); setDirty(false) })
      .catch(console.error)
  }

  /* -------- render -------- */
  return (
    <main className="main-content">
      <h1>Gestión de productos</h1>

      {/* -------- TARJETAS -------- */}
      <div className="product-cards">
        {items.map(p => (
          <div key={p.id} className="product-card-admin">
            <img
              src={p.imageUrl || "/placeholder.png"}
              alt={p.nombre}
            />

            <div className="card-fields">
              <label>
                Nombre
                <input
                  value={p.nombre}
                  onChange={e => handleChange(p.id,"nombre",e.target.value)}
                  disabled={!editing}
                />
              </label>

              <label>
                Descripción
                <textarea
                  value={p.descripcion}
                  onChange={e => handleChange(p.id,"descripcion",e.target.value)}
                  disabled={!editing}
                />
              </label>

              <div className="inline">
                <label>
                  Precio
                  <input
                    type="number"
                    value={p.precio}
                    onChange={e => handleChange(p.id,"precio",Number(e.target.value))}
                    disabled={!editing}
                  />
                </label>

                <label>
                  Stock
                  <input
                    type="number"
                    value={p.stock}
                    onChange={e => handleChange(p.id,"stock",Number(e.target.value))}
                    disabled={!editing}
                  />
                </label>

                <label>
                  Tag
                  <input
                    value={p.tag || ""}
                    onChange={e => handleChange(p.id,"tag",e.target.value)}
                    disabled={!editing}
                  />
                </label>
              </div>
            </div>

            {!editing && (
              <button className="btn-danger" onClick={() => handleDelete(p.id)}>
                Eliminar
              </button>
            )}
          </div>
        ))}
      </div>

      {/* -------- BOTÓN EDITAR / GUARDAR -------- */}
      <div className="admin-actions">
        {editing ? (
          <button
            className="btn-warning"
            disabled={!dirty}
            onClick={handleSave}
          >
            Guardar cambios
          </button>
        ) : (
          <button className="btn-success" onClick={() => setEditing(true)}>
            Editar
          </button>
        )}
      </div>
    </main>
  )
}
