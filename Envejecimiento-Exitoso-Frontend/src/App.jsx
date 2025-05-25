// src/App.jsx
import React, { useState, useContext } from "react"
import { Routes, Route, Navigate } from "react-router-dom"

/* ------------ CONTEXTOS ------------ */
import { AuthContext } from "./context/AuthContext"
import { ProductsContext } from "./context/ProductsContext"
/* ------------ COMPONENTES ------------ */
import Navbar        from "./components/Navbar"
import SidebarAuth   from "./components/SidebarAuth"

/* ------------ PÁGINAS ------------ */
import Catalog       from "./pages/Catalog"
import Cart          from "./pages/Cart"
import Orders        from "./pages/Orders"
import Payment       from "./pages/Payment"
import Confirm       from "./pages/Confirm"
import Login         from "./pages/Login"
import Register      from "./pages/Register"
import ProductDetail from "./pages/ProductDetail"
import AddProduct    from "./components/AddProduct"
import AdminProducts from "./pages/AdminProducts"

export default function App() {
  const [authOpen, setAuthOpen] = useState(false)
  const { user } = useContext(AuthContext)
  const isAdmin  = user?.role === "ADMIN"

  /* ---- wrapper para proteger rutas admin ---- */
  const RequireAdmin = ({ children }) =>
    isAdmin ? children : <Navigate to="/" replace />

  return (
    <>
      <Navbar onAuthOpen={() => setAuthOpen(true)} />
      <SidebarAuth open={authOpen} onClose={() => setAuthOpen(false)} />

      <Routes>
        {/* PÚBLICAS */}
        <Route path="/"            element={<Catalog />} />
        <Route path="/producto/:id" element={<ProductDetail />} />
        <Route path="/cart"        element={<Cart />} />
        <Route path="/orders"      element={<Orders />} />
        <Route path="/payment"     element={<Payment />} />
        <Route path="/confirm"     element={<Confirm />} />
        <Route path="/login"       element={<Login />} />
        <Route path="/register"    element={<Register />} />

        {/* ADMIN */}
        <Route
          path="/admin/productos"
          element={
            <RequireAdmin>
              <AdminProducts />
            </RequireAdmin>
          }
        />
        <Route
          path="/admin/productos/nuevo"
          element={
            <RequireAdmin>
              <AddProduct />
            </RequireAdmin>
          }
        />

        {/* CUALQUIER OTRA URL */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  )
}
