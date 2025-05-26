// ┌────────────────────────────────────────────────────┐
// │ src/pages/Login.jsx                                │
// └────────────────────────────────────────────────────┘
import React, { useState, useContext } from "react";
import { useNavigate }                  from "react-router-dom";
import { AuthContext }                  from "../context/AuthContext";   // ⬅️  nuevo

export default function Login() {
  const navigate   = useNavigate();
  const { login }  = useContext(AuthContext);                           // ⬅️  nuevo
  const [email, setEmail]     = useState("");
  const [password, setPwd]    = useState("");

  /* ------------ submit ------------ */
  async function handleSubmit(e) {
    e.preventDefault();
    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/auth/login`, {
        method : "POST",
        headers: { "Content-Type": "application/json" },
        body   : JSON.stringify({ email, password })
      });
      if (!res.ok) throw new Error("Credenciales inválidas");
      const { accessToken, refreshToken, role } = await res.json();
      login({ name: email, role, accessToken, refreshToken });          // ⬅️  usa contexto
      navigate("/");
    } catch (err) { alert(err.message); }
  }

  /* ------------ UI ------------ */
  return (
    <div style={{
      maxWidth: 370, margin: "56px auto", padding: "34px 26px",
      background: "#fff", borderRadius: 15, boxShadow: "0 2px 14px #0001"
    }}>
      <h2 style={{ textAlign:"center", fontWeight:800, marginBottom:24 }}>
        Iniciar Sesión
      </h2>

      <form onSubmit={handleSubmit}>
        <label style={{ fontWeight:600 }}>Correo Electrónico</label>
        <input
          type="email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          placeholder="ejemplo@dominio.com"
          style={{
            width:"100%", marginBottom:14, padding:"9px 7px",
            borderRadius:5, border:"1.5px solid #e0e5f3", fontSize:15
          }}
          required
        />

        <label style={{ fontWeight:600 }}>Contraseña</label>
        <input
          type="password"
          value={password}
          onChange={e => setPwd(e.target.value)}
          style={{
            width:"100%", marginBottom:18, padding:"9px 7px",
            borderRadius:5, border:"1.5px solid #e0e5f3", fontSize:15
          }}
          required
        />

        <div style={{ display:"flex", gap:12 }}>
          <button type="submit" style={{
            flex:1, background:"#2385e6", color:"#fff", fontWeight:700,
            border:"none", borderRadius:5, padding:"10px 0", fontSize:16
          }}>
            Entrar
          </button>

          <button type="button" onClick={() => navigate("/register")} style={{
            flex:1, background:"#e6e6e6", color:"#317ada", fontWeight:700,
            border:"none", borderRadius:5, padding:"10px 0", fontSize:16
          }}>
            Registrarse
          </button>
        </div>
      </form>
    </div>
  );
}
