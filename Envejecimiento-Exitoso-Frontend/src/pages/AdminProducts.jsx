// src/pages/AdminProducts.jsx
import React, {
  useState,
  useContext,
  useEffect            // ← importado
} from "react"
import { useNavigate }         from "react-router-dom"
import { AuthContext }         from "../context/AuthContext"
import { ProductsContext }     from "../context/ProductsContext"
import { resolveImg } from "../utils/resolveImg";
import "../css/global.css"

export default function AdminProducts() {
  /* ------------------------------------------------------------------ */
  const { user } = useContext(AuthContext)
  const {
    products: items,
    fetchProducts,
    deleteProduct,
    updateProduct
  } = useContext(ProductsContext)

  const navigate = useNavigate()

  /* --- redirección si NO es admin --- */
  if (user?.role !== "ADMIN") {
    navigate("/")
    return null
  }

  /* ------------------------------------------------------------------ */
  const [editing, setEditing] = useState(false)
  const [dirty,   setDirty]   = useState(false)

  /* --- carga (sólo si aún no hay data) --- */
  useEffect(() => {
    if (items.length === 0) fetchProducts()
  }, [items, fetchProducts])

  /* --- local copy para edición --- */
  const [drafts, setDrafts] = useState(items)
  useEffect(() => setDrafts(items), [items])

  /* ------------------------------------------------------------------ */
  const handleChange = (id, field, value) => {
    setDirty(true)
    setDrafts(prev =>
      prev.map(p => (p.id === id ? { ...p, [field]: value } : p))
    )
  }

  const handleDelete = id =>
    window.confirm("¿Eliminar producto?") && deleteProduct(id)

  const handleSave = () =>
    Promise.all(drafts.map(updateProduct))
      .then(() => {
        setEditing(false)
        setDirty(false)
      })
      .catch(console.error)

  /* ------------------------------------------------------------------ */
  return (
    <main className="main-content">
      <h1>Gestión de productos</h1>

      {/* -------- TARJETAS -------- */}
      <div className="product-cards">
        {drafts.map(p => (
          <div key={p.id} className="product-card-admin">
            <img
                src={resolveImg(p.imageUrl || p.imagenUrl)}
                alt={p.nombre || p.nombreProducto || "Producto"}
                 onError={e => {
                     e.currentTarget.onerror = null;
                     e.currentTarget.src = "/placeholder.png";
                 }}
                />


            <div className="card-fields">
              <label>
                Nombre
                <input
                  value={p.nombre}
                  onChange={e => handleChange(p.id, "nombre", e.target.value)}
                  disabled={!editing}
                />
              </label>

              <label>
                Descripción
                <textarea
                  value={p.descripcion}
                  onChange={e =>
                    handleChange(p.id, "descripcion", e.target.value)
                  }
                  disabled={!editing}
                />
              </label>

              <div className="inline">
                <label>
                  Precio
                  <input
                    type="number"
                    value={p.precio}
                    onChange={e =>
                      handleChange(p.id, "precio", Number(e.target.value))
                    }
                    disabled={!editing}
                  />
                </label>

                <label>
                  Stock
                  <input
                    type="number"
                    value={p.stock}
                    onChange={e =>
                      handleChange(p.id, "stock", Number(e.target.value))
                    }
                    disabled={!editing}
                  />
                </label>

                <label>
                  Tag
                  <input
                    value={p.tag || ""}
                    onChange={e => handleChange(p.id, "tag", e.target.value)}
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

      {/* -------- BOTONES -------- */}
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
