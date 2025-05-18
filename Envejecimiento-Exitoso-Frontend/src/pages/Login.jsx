import React, { useState } from "react"
import { useNavigate } from "react-router-dom"

export default function Login() {
  const navigate = useNavigate()
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  function handleSubmit(e) {
    e.preventDefault()
    // Aquí conectarías con el backend de login...
    alert("Inicio de sesión simulado. ¡Redirigiendo al home!")
    navigate("/")
  }

  return (
    <div style={{
      maxWidth: 370, margin: "56px auto", padding: "34px 26px",
      background: "#fff", borderRadius: 15, boxShadow: "0 2px 14px #0001"
    }}>
      <h2 style={{ textAlign: "center", fontWeight: 800, marginBottom: 24 }}>Iniciar Sesión</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label style={{ fontWeight: 600 }}>Correo Electrónico</label>
          <input
            type="email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            placeholder="ejemplo@dominio.com"
            style={{
              width: "100%", marginBottom: 14, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div>
          <label style={{ fontWeight: 600 }}>Contraseña</label>
          <input
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            style={{
              width: "100%", marginBottom: 18, padding: "9px 7px",
              borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
            }}
            required
          />
        </div>
        <div style={{ display: "flex", gap: 12 }}>
          <button
            type="submit"
            style={{
              flex: 1,
              background: "#2385e6", color: "#fff", fontWeight: 700,
              border: "none", borderRadius: 5, padding: "10px 0",
              fontSize: 16, cursor: "pointer"
            }}
          >
            Iniciar Sesión
          </button>
          <button
            type="button"
            style={{
              flex: 1,
              background: "#e6e6e6", color: "#317ada", fontWeight: 700,
              border: "none", borderRadius: 5, padding: "10px 0",
              fontSize: 16, cursor: "pointer"
            }}
            onClick={() => navigate("/register")}
          >
            Registrarse
          </button>
        </div>
        <div style={{ marginTop: 10, textAlign: "center" }}>
          <a href="#" style={{ color: "#2976e0", fontSize: 15, textDecoration: "underline" }}>
            ¿Olvidaste tu contraseña?
          </a>
        </div>
      </form>
    </div>
  )
}
