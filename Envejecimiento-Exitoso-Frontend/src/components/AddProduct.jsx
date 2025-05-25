import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/addProduct.css';

export default function AddProduct() {
  const [form, setForm] = useState({
    nombre: '',
    descripcion: '',
    precio: '',
    stock: '',
    imageUrl: '',
    tag: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = e =>
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    try {
      const token = localStorage.getItem('accessToken');
      const res = await fetch(`${import.meta.env.VITE_API_URL}/api/productos`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          nombre: form.nombre,
          descripcion: form.descripcion,
          precio: parseFloat(form.precio),
          stock: parseInt(form.stock, 10),
          imageUrl: form.imageUrl,
          tag: form.tag
        })
      });
      if (!res.ok) throw new Error('No se pudo crear el producto');
      navigate('/');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="add-product">
      <h2>Nuevo producto</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        {/* Nombre */}
        <div className="input-group">
          <label htmlFor="nombre">Nombre</label>
          <input
            id="nombre"
            name="nombre"
            placeholder="Nombre del producto"
            value={form.nombre}
            onChange={handleChange}
            required
          />
        </div>

        {/* Descripción */}
        <div className="input-group">
          <label htmlFor="descripcion">Descripción</label>
          <textarea
            id="descripcion"
            name="descripcion"
            placeholder="Descripción"
            value={form.descripcion}
            onChange={handleChange}
            required
          />
        </div>

        {/* Precio */}
        <div className="input-group">
          <label htmlFor="precio">Precio</label>
          <input
            id="precio"
            name="precio"
            type="number"
            step="0.01"
            placeholder="0.00"
            value={form.precio}
            onChange={handleChange}
            required
          />
        </div>

        {/* Stock */}
        <div className="input-group">
          <label htmlFor="stock">Stock</label>
          <input
            id="stock"
            name="stock"
            type="number"
            placeholder="0"
            value={form.stock}
            onChange={handleChange}
            required
          />
        </div>

        {/* URL de imagen */}
        <div className="input-group">
          <label htmlFor="imageUrl">URL de la imagen</label>
          <input
            id="imageUrl"
            name="imageUrl"
            type="text"
            placeholder="/uploads/"
            value={form.imageUrl}
            onChange={handleChange}
          />
          {form.imageUrl && (
            <div className="image-preview">
              <img src={form.imageUrl} alt="Previsualización" />
            </div>
          )}
        </div>

        {/* Tag / Identificador */}
        <div className="input-group">
          <label htmlFor="tag">Etiqueta (para navbar)</label>
          <input
            id="tag"
            name="tag"
            placeholder="Ej: novedades"
            value={form.tag}
            onChange={handleChange}
          />
          {form.tag && (
            <div className="tag-list">
              <span>{form.tag}</span>
            </div>
          )}
        </div>

        <button type="submit">Crear producto</button>
      </form>
    </div>
  );
}
