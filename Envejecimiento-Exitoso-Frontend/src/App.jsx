// src/App.jsx
import React from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

import Navbar        from './components/Navbar'
import CartDropdown  from './components/CartDropdown'

import Home     from './pages/Home'
import Catalog  from './pages/Catalog'
import Cart     from './pages/Cart'
import Confirm  from './pages/Confirm'
import Payment  from './pages/Payment'
import Login    from './pages/Login'
import Register from './pages/Register'

export default function App() {
  return (
    <>
      {/* Siempre visible */}
      <Navbar />
      <CartDropdown />
      

      <Routes>
        <Route path="/"         element={<Home />} />
        <Route path="/catalog"  element={<Catalog />} />
        <Route path="/cart"     element={<Cart />} />
        <Route path="/login"    element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/payment"  element={<Payment />} />
        <Route path="/confirm"  element={<Confirm />} />

        {/* Cualquier otro path, redirige a Home */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
   </>
  );
}
