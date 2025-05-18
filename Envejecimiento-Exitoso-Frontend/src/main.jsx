// src/main.jsx
import React from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { CartProvider  } from './context/CartContext'
 
// ----------------------------------------
//      IMPORT STYLES
// ----------------------------------------
import './css/style.css';
import './css/login.css';
import './css/registro.css';
import './css/catalog.css';
import './css/cart.css';
import './css/payment.css';
import './css/confirm.css';

// monta la app con la función importada
createRoot(document.getElementById('root')).render(
<React.StrictMode>
    <BrowserRouter>
      <CartProvider>
        <App />
      </CartProvider>
    </BrowserRouter>
  </React.StrictMode>
)