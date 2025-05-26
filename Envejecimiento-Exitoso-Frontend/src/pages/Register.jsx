// src/pages/Register.jsx
import React, { useState } from "react"
import { useNavigate } from "react-router-dom"
import "../css/registro.css"

export default function Register() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    nombreCompleto: "",
    email: "",
    password: "",
    confirmPassword: "",
    rfc: ""
  })
  const [error, setError] = useState("")

  const handleChange = e =>{
    console.log("CHANGE", e.target.name, e.target.value);
    
    setForm(f => ({ ...f, [e.target.name]: e.target.value }))}

  const handleSubmit = async e => {
    e.preventDefault()
    setError("")
    console.log("SUMBIT", form);
    
    if (form.password !== form.confirmPassword) {
      setError("Las contraseñas no coinciden")
      return
    }

    try {
      const res = await fetch(
        `${import.meta.env.VITE_API_URL}/api/clientes`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            nombreCompleto:    form.nombreCompleto,
            email:     form.email,
            password:  form.password,
            rfc:       form.rfc
          })
        }
      )
      if (!res.ok) {
        const body = await res.json().catch(() => ({}))
        throw new Error(body.message || "Registro fallido")
      }
      alert("¡Registro exitoso!")
      navigate("/login")
    } catch (err) {
      console.error(err)
      setError(err.message)
    }
  }

  return (
    <div className="register-container">
      <h2>Registro</h2>
      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleSubmit}>
        <label>Nombre Completo</label>
        <input
          name="nombreCompleto"
          value={form.nombre}
          onChange={handleChange}
          required
        />

        <label>Email</label>
        <input
          type="email"
          name="email"
          value={form.email}
          onChange={handleChange}
          required
        />

        <label>Contraseña</label>
        <input
          type="password"
          name="password"
          value={form.password}
          onChange={handleChange}
          required
        />

        <label>Confirmar contraseña</label>
        <input
          type="password"
          name="confirmPassword"
          value={form.confirmPassword}
          onChange={handleChange}
          required
        />
        <label>RFC</label>
        <input
          name="rfc"
          value={form.rfc}
          onChange={handleChange}
          required
        />


        <button type="submit" className="register-btn">
          Registrar
        </button>

        <div className="login-link">
          ¿Ya tienes cuenta?{" "}
          <a
            href="#"
            onClick={e => {
              e.preventDefault()
              navigate("/login")
            }}
          >
            Inicia sesión
          </a>
        </div>
      </form>
    </div>
  )
  console.log("RENDER", form);
  
}
