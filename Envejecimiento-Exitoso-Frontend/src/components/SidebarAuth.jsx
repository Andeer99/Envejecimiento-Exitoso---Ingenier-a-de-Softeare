import React, { useState } from "react";

export default function SidebarAuth({ open, onClose }) {
  const [isLogin, setIsLogin] = useState(true);
  const [login, setLogin] = useState({ email: "", password: "" });
  const [register, setRegister] = useState({
    nombre: "",
    email: "",
    password: "",
    confirmPassword: "",
    direccion: "",
    telefono: "",
  });

  // Funciones de formulario
  function handleLogin(e) {
    e.preventDefault();
    alert("Inicio de sesión simulado!");
    onClose();
  }

  function handleRegister(e) {
    e.preventDefault();
    if (register.password !== register.confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }
    alert("¡Registro simulado exitoso!");
    setIsLogin(true);
  }

  // Estilo base de la barra lateral
  const sidebarStyle = {
    position: "fixed",
    top: 0,
    right: open ? 0 : "-410px",
    width: 390,
    height: "100vh",
    background: "#fff",
    boxShadow: "0 0 25px #0002",
    transition: "right .35s cubic-bezier(.6,.1,.39,1.03)",
    zIndex: 1999,
    padding: "34px 28px 0 28px",
    overflowY: "auto"
  };

  return (
    <>
      {open && (
  <div
    onClick={onClose}
    style={{
      position: "fixed",
      top: 0, left: 0, right: 0, bottom: 0,
      background: "#2227", zIndex: 1998,
      opacity: 1, // Siempre opaco cuando está abierto
      transition: "opacity .3s"
    }}
  />
)}
      {/* SIDEBAR */}
      <aside style={sidebarStyle}>
        {/* BOTÓN DE CIERRE */}
        <button
          onClick={onClose}
          style={{
            position: "absolute", right: 20, top: 20, fontSize: 28,
            background: "none", border: "none", color: "#aaa", cursor: "pointer"
          }}
          title="Cerrar"
        >×</button>
        {/* FORMULARIO DE LOGIN */}
        {isLogin ? (
          <>
            <h2 style={{ textAlign: "center", fontWeight: 800, marginBottom: 24 }}>
              Iniciar Sesión
            </h2>
            <form onSubmit={handleLogin}>
              <div>
                <label style={{ fontWeight: 600 }}>Correo Electrónico</label>
                <input
                  type="email"
                  value={login.email}
                  onChange={e => setLogin({ ...login, email: e.target.value })}
                  placeholder="ejemplo@dominio.com"
                  style={inputStyle}
                  required
                />
              </div>
              <div>
                <label style={{ fontWeight: 600 }}>Contraseña</label>
                <input
                  type="password"
                  value={login.password}
                  onChange={e => setLogin({ ...login, password: e.target.value })}
                  style={inputStyle}
                  required
                />
              </div>
              <div style={{ display: "flex", gap: 12 }}>
                <button
                  type="submit"
                  style={buttonStyleBlue}
                >
                  Iniciar Sesión
                </button>
                <button
                  type="button"
                  style={buttonStyleGrey}
                  onClick={() => setIsLogin(false)}
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
          </>
        ) : (
          <>
            <h2 style={{ textAlign: "center", fontWeight: 800, marginBottom: 24 }}>
              Registro
            </h2>
            <form onSubmit={handleRegister}>
              <div>
                <label style={{ fontWeight: 600 }}>Nombre Completo</label>
                <input
                  type="text"
                  name="nombre"
                  value={register.nombre}
                  onChange={e => setRegister({ ...register, nombre: e.target.value })}
                  placeholder="Tu nombre completo"
                  style={inputStyle}
                  required
                />
              </div>
              <div>
                <label style={{ fontWeight: 600 }}>Correo Electrónico</label>
                <input
                  type="email"
                  name="email"
                  value={register.email}
                  onChange={e => setRegister({ ...register, email: e.target.value })}
                  placeholder="ejemplo@dominio.com"
                  style={inputStyle}
                  required
                />
              </div>
              <div>
                <label style={{ fontWeight: 600 }}>Contraseña</label>
                <input
                  type="password"
                  name="password"
                  value={register.password}
                  onChange={e => setRegister({ ...register, password: e.target.value })}
                  style={inputStyle}
                  required
                />
              </div>
              <div>
                <label style={{ fontWeight: 600 }}>Confirmar Contraseña</label>
                <input
                  type="password"
                  name="confirmPassword"
                  value={register.confirmPassword}
                  onChange={e => setRegister({ ...register, confirmPassword: e.target.value })}
                  style={inputStyle}
                  required
                />
              </div>
              <div>
                <label style={{ fontWeight: 600 }}>Dirección</label>
                <input
                  type="text"
                  name="direccion"
                  value={register.direccion}
                  onChange={e => setRegister({ ...register, direccion: e.target.value })}
                  placeholder="Tu dirección"
                  style={inputStyle}
                  required
                />
              </div>
              <div>
                <label style={{ fontWeight: 600 }}>Teléfono</label>
                <input
                  type="tel"
                  name="telefono"
                  value={register.telefono}
                  onChange={e => setRegister({ ...register, telefono: e.target.value })}
                  placeholder="Número de teléfono"
                  style={inputStyle}
                  required
                />
              </div>
              <button
                type="submit"
                style={buttonStyleBlue}
              >
                Registrar
              </button>
              <div style={{ marginTop: 10, textAlign: "center" }}>
                <a href="#"
                  style={{ color: "#2976e0", fontSize: 15, textDecoration: "underline" }}
                  onClick={e => { e.preventDefault(); setIsLogin(true); }}
                >
                  ¿Ya tienes cuenta? Inicia sesión
                </a>
              </div>
            </form>
          </>
        )}
      </aside>
    </>
  );
}

// Estilos reutilizables
const inputStyle = {
  width: "100%", marginBottom: 10, padding: "9px 7px",
  borderRadius: 5, border: "1.5px solid #e0e5f3", fontSize: 15
};

const buttonStyleBlue = {
  flex: 1,
  background: "#2385e6", color: "#fff", fontWeight: 700,
  border: "none", borderRadius: 5, padding: "10px 0",
  fontSize: 16, cursor: "pointer"
};

const buttonStyleGrey = {
  flex: 1,
  background: "#e6e6e6", color: "#317ada", fontWeight: 700,
  border: "none", borderRadius: 5, padding: "10px 0",
  fontSize: 16, cursor: "pointer"
};
