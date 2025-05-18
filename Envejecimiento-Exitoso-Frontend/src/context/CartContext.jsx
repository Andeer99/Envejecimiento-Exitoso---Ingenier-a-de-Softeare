// src/context/CartContext.jsx
import React, { createContext, useEffect, useState } from "react"

export const CartContext = createContext()

export function CartProvider({ children }) {
  const [cart, setCart] = useState([])

  // Cargar del localStorage al iniciar
  useEffect(() => {
    const data = localStorage.getItem("cart")
    if (data) setCart(JSON.parse(data))
  }, [])

  // Guardar cada vez que cambia
  useEffect(() => {
    localStorage.setItem("cart", JSON.stringify(cart))
  }, [cart])

  function addToCart(producto) {
  setCart(prev => {
    const found = prev.find(p => p.id === producto.id)
    const cantidadAAgregar = producto.cantidad || 1
    if (found) {
      return prev.map(p =>
        p.id === producto.id
          ? { ...p, cantidad: p.cantidad + cantidadAAgregar }
          : p
      )
    } else {
      // SIEMPRE agrega cantidadAAgregar como cantidad
      return [...prev, { ...producto, cantidad: cantidadAAgregar }]
    }
  })
}


  function removeFromCart(id) {
    setCart(prev => prev.filter(p => p.id !== id))
  }

  function clearCart() {
    setCart([])
  }
  function updateQuantity(id, cantidad) {
  setCart(prev =>
    prev.map(item =>
      item.id === id ? { ...item, cantidad } : item
    )
  )
}

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, clearCart, updateQuantity }}>
      {children}
    </CartContext.Provider>
  )
}
