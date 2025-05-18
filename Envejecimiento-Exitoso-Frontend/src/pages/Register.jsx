import React, { useState } from "react"
import { useNavigate } from "react-router-dom"

export default function Register() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    nombre: "", email: "", password: "",
    confirmPassword: "", direccion: "", telefono: ""
  })

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (form.password !== form.confirmPassword) {
      alert("Las contraseñas no coinciden.")
      return
    }
    // Aquí conectarías con el backend de registro...
    alert("¡Registro simulado exitoso! Redirigiendo a login")
    navigate("/login")
  }

  return (
    <div style={{
      maxWidth: 390, margin: "56px auto", padding: "34px 26px",
      background: "#fff", borderRadius: 15, boxShadow: "0 2px 14px #0001"
    }}>
      <h2 style={{ textAlign: "center", fontWeight: 800, marginBottom: 24 }}>Registro</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label style={{ fontWeight: 600 }}>Nombre Completo</label>
          <input
            type="text"
            name="nombre"
            value={form.nombre}
            onChange={handleChange}
            placeholder="Tu nombre completo"
            style={{
              width: "100%", marginBottom: 10, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div>
          <label style={{ fontWeight: 600 }}>Correo Electrónico</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            placeholder="ejemplo@dominio.com"
            style={{
              width: "100%", marginBottom: 10, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div>
          <label style={{ fontWeight: 600 }}>Contraseña</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            style={{
              width: "100%", marginBottom: 10, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div>
          <label style={{ fontWeight: 600 }}>Confirmar Contraseña</label>
          <input
            type="password"
            name="confirmPassword"
            value={form.confirmPassword}
            onChange={handleChange}
            style={{
              width: "100%", marginBottom: 10, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div>
          <label style={{ fontWeight: 600 }}>Dirección</label>
          <input
            type="text"
            name="direccion"
            value={form.direccion}
            onChange={handleChange}
            placeholder="Tu dirección"
            style={{
              width: "100%", marginBottom: 10, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div>
          <label style={{ fontWeight: 600 }}>Teléfono</label>
          <input
            type="tel"
            name="telefono"
            value={form.telefono}
            onChange={handleChange}
            placeholder="Número de teléfono"
            style={{
              width: "100%", marginBottom: 18, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <button
          type="submit"
          style={{
            width: "100%", background: "#2385e6", color: "#fff",
            fontWeight: 700, border: "none", borderRadius: 5,
            padding: "12px 0", fontSize: 17, cursor: "pointer"
          }}
        >
          Registrar
        </button>
        <div style={{ marginTop: 10, textAlign: "center" }}>
          <a href="#" style={{ color: "#2976e0", fontSize: 15, textDecoration: "underline" }}
            onClick={e => { e.preventDefault(); navigate("/login") }}>
            ¿Ya tienes cuenta? Inicia sesión
          </a>
        </div>
      </form>
    </div>
  )
}
