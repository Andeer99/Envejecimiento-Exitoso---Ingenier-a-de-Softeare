// src/context/AuthContext.jsx
import React, { createContext, useEffect, useState } from "react"

export const AuthContext = createContext()

export function AuthProvider({ children }) {
  /* -------- helper -------- */
  const readUserFromStorage = () => {
    const token = localStorage.getItem("accessToken")
    if (!token) return null           // no log-in

    return {
      name:  localStorage.getItem("userEmail") ?? "Usuario",
      role:  localStorage.getItem("userRole")  ?? "USER",
      accessToken:  token,
      refreshToken: localStorage.getItem("refreshToken") ?? null
    }
  }

  /* -------- state -------- */
  const [user, setUser] = useState(readUserFromStorage)

  /* -------- persist -------- */
  useEffect(() => {
    if (!user) {
      // limpieza
      localStorage.removeItem("accessToken")
      localStorage.removeItem("refreshToken")
      localStorage.removeItem("userRole")
      localStorage.removeItem("userEmail")
    }
    // si usuario ≠ null no necesitamos guardar nada extra:
    // ya lo hace tu flujo de login
  }, [user])

  /* -------- acciones -------- */
  function login({ name, role, accessToken, refreshToken }) {
    // guarda en LS para la próxima sesión
    localStorage.setItem("accessToken",  accessToken)
    localStorage.setItem("refreshToken", refreshToken)
    localStorage.setItem("userRole",     role)
    localStorage.setItem("userEmail",    name)

    setUser({ name, role, accessToken, refreshToken })
  }

  function logout() {
    setUser(null)
  }

  function updateUser(fields) {
    setUser(prev => ({ ...prev, ...fields }))
  }

  return (
    <AuthContext.Provider value={{ user, login, logout, updateUser }}>
      {children}
    </AuthContext.Provider>
  )
}
