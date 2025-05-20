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
    fetch(`${import.meta.env.VITE_API_URL}/clientes`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: form.nombre,
        email: form.email,
        password: form.password,
        direccion: form.direccion,
        telefono: form.telefono
      })
    })
      .then(res => {
        if (!res.ok) throw new Error("Registro fallido")
        return res.json()
      })
      .then(() => {
        alert("¡Registro exitoso!")
        navigate("/login")
      })
      .catch(() => alert("Error al registrar usuario"))
  }

  return (
    <div style={{ maxWidth: 390, margin: "56px auto", padding: "34px 26px", background: "#fff", borderRadius: 15, boxShadow: "0 2px 14px #0001" }}>
      <h2 style={{ textAlign: "center", fontWeight: 800, marginBottom: 24 }}>Registro</h2>
      <form onSubmit={handleSubmit}>
        {/* ...todos los inputs igual que antes, usando handleChange y form.* */}
        {/* Botón */}
        <button type="submit" style={{ width: "100%", background: "#2385e6", color: "#fff", fontWeight: 700, border: "none", borderRadius: 5, padding: "12px 0", fontSize: 17, cursor: "pointer" }}>
          Registrar
        </button>
        {/* Link a login */}
        <div style={{ marginTop: 10, textAlign: "center" }}>
          <a href="#" style={{ color: "#2976e0", fontSize: 15, textDecoration: "underline" }} onClick={e => { e.preventDefault(); navigate("/login") }}>
            ¿Ya tienes cuenta? Inicia sesión
          </a>
        </div>
      </form>
    </div>
  )
}
