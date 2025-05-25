import React, { useState } from "react";
import { useNavigate }       from "react-router-dom";
import "../css/addProduct.css";

export default function AddProduct() {
  const apiBase = import.meta.env.VITE_API_URL;          // ← base del backend
  const navigate = useNavigate();

  /* ----------------------------- estado ----------------------------- */
  const [form,  setForm]  = useState({
    nombre: "",
    descripcion: "",
    precio: "",
    stock: "",
    imageUrl: "",      // aquí almacenaremos /uploads/xxxx.png
    tag: ""
  });
  const [error, setError] = useState("");

  /* ---------------------- handlers de formulario -------------------- */
  const handleChange = e =>
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  /** Sube la imagen y guarda la ruta que devuelve el backend */
  const handleFile = async e => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      const fd    = new FormData();
      fd.append("file", file);

      const token = localStorage.getItem("accessToken");
      const res   = await fetch(`${apiBase}/api/upload`, {
        method : "POST",
        headers: { Authorization: `Bearer ${token}` }, // *NO* pongas Content-Type
        body   : fd
      });

      const data = await res.json();
      if (!res.ok || !data.url)
        throw new Error(data.message || "Error al subir la imagen");

      // ⚠️  Importante: guardamos EXACTAMENTE la ruta que vino del backend
      //     (normalmente "/uploads/uuid.png")
      setForm(f => ({ ...f, imageUrl: data.url }));
    } catch (err) {
      setError(err.message || "Error inesperado");
    }
  };

  /** Envía el nuevo producto */
  const handleSubmit = async e => {
    e.preventDefault();
    setError("");

    try {
      const token = localStorage.getItem("accessToken");
      const res   = await fetch(`${apiBase}/api/productos`, {
        method : "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization : `Bearer ${token}`
        },
        body: JSON.stringify({
          ...form,
          precio: parseFloat(form.precio),
          stock : parseInt(form.stock, 10)
        })
      });

      if (!res.ok) throw new Error("No se pudo crear el producto");
      navigate("/");
    } catch (err) {
      setError(err.message);
    }
  };

  /* ---------------------------- render ----------------------------- */
  return (
    <main className="add-product">
      <h1>Nuevo producto</h1>
      {error && <p className="error">{error}</p>}

      <form onSubmit={handleSubmit}>
        {/* Nombre */}
        <label>Nombre
          <input
            name="nombre"
            value={form.nombre}
            onChange={handleChange}
            required
          />
        </label>

        {/* Descripción */}
        <label>Descripción
          <textarea
            name="descripcion"
            value={form.descripcion}
            onChange={handleChange}
            required
          />
        </label>

        {/* Precio */}
        <label>Precio
          <input
            type="number"
            step="0.01"
            name="precio"
            value={form.precio}
            onChange={handleChange}
            required
          />
        </label>

        {/* Stock */}
        <label>Stock
          <input
            type="number"
            name="stock"
            value={form.stock}
            onChange={handleChange}
            required
          />
        </label>

        {/* Imagen: subir archivo */}
        <label>Imagen
          <input type="file" accept="image/*" onChange={handleFile} />
        </label>

        {/* Campo solo-lectura con la URL devuelta */}
        <label>URL (auto)
          <input
            name="imageUrl"
            value={form.imageUrl}
            readOnly
          />
        </label>

        {/* Tag opcional */}
        <label>Tag
          <input
            name="tag"
            value={form.tag}
            onChange={handleChange}
          />
        </label>

        <button className="btn-success" type="submit">Crear producto</button>
      </form>
    </main>
  );
}
