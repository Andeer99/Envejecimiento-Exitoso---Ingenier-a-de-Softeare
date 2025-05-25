// src/main.jsx
import React from "react"
import { createRoot } from "react-dom/client"
import { BrowserRouter } from "react-router-dom"

import App from "./App"
import { CartProvider }  from "./context/CartContext"
import { AuthProvider }  from "./context/AuthContext"   // ← importa el Provider
import { ProductsContext, ProductsProvider } from "./context/ProductsContext"
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
      <AuthProvider>
        <ProductsProvider>        
            <CartProvider>        
              <App />
            </CartProvider>
        </ProductsProvider>  
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
)
