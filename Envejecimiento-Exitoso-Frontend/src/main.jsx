// src/main.jsx
import React from "react"
import { createRoot } from "react-dom/client"
import { BrowserRouter } from "react-router-dom"

import App from "./App"
import { CartProvider }  from "./context/CartContext"
import { AuthProvider }  from "./context/AuthContext"   // ← importa el Provider

/* -------------------------  STYLES  ------------------------- */
import "./css/global.css"
import "./css/login.css"
import "./css/registro.css"
import "./css/cart.css"
import "./css/payment.css"
import "./css/confirm.css"

/* -------------------------  APP MOUNT  ----------------------- */
createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>          {/* 1º: autenticación */}
        <CartProvider>        {/* 2º: carrito */}
          <App />
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
)
