import React, { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'

import Navbar from './components/Navbar'

import Home from './pages/Home'
import Orders from './pages/Orders'
import Catalog from './pages/Catalog'   // <--- CORRECTO
import Cart from './pages/Cart'
import Confirm from './pages/Confirm'
import Payment from './pages/Payment'
import Login from './pages/Login'
import Register from './pages/Register'
import ProductDetail from './pages/ProductDetail'
import SidebarAuth from './components/SidebarAuth'

export default function App() {
  const [authOpen, setAuthOpen] = useState(false);

  return (
    <>
      <Navbar onAuthOpen={() => setAuthOpen(true)} />
      <SidebarAuth open={authOpen} onClose={() => setAuthOpen(false)} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/catalog" element={<Catalog />} />   {/* <--- SÓLO UNA VEZ */}
        <Route path="/cart" element={<Cart />} />
        <Route path="/login" element={<Login />} />
        <Route path="/orders" element={<Orders />} />
        <Route path="/register" element={<Register />} />
        <Route path="/payment" element={<Payment />} />
        <Route path="/confirm" element={<Confirm />} />
        <Route path="/producto/:id" element={<ProductDetail />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  )
}
