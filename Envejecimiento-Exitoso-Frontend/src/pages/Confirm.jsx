import React, { useEffect, useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Confirm() {
  const navigate = useNavigate()
  const [secondsLeft, setSecondsLeft] = useState(10)
  const [showCountdown, setShowCountdown] = useState(false)
  const timerRef = useRef(null)
  const orderId = '#11111' 

  useEffect(() => {
    timerRef.current = setInterval(() => {
      setSecondsLeft(prev => prev - 1)
    }, 1000)
    return () => clearInterval(timerRef.current)
  }, [])

  useEffect(() => {
    if (secondsLeft === 5) setShowCountdown(true)
    if (secondsLeft === 0) {
      clearInterval(timerRef.current)
      navigate("/")
    }
  }, [secondsLeft, navigate])

  function goToOrders() {
    clearInterval(timerRef.current)
    navigate("/orders")
  }
  function goToHome() {
    clearInterval(timerRef.current)
    navigate("/")
  }

  return (
    <div style={{
      maxWidth: 480,
      margin: "56px auto",
      padding: "36px 26px",
      background: "#fff",
      borderRadius: 15,
      boxShadow: "0 2px 14px #0001",
      textAlign: "center",
      position: "relative"
    }}>
      <h2 style={{ color: "#21a03c", fontWeight: 800, marginBottom: 14, fontSize: 26 }}>¡Gracias por tu compra!</h2>
      <div style={{ fontSize: 16, marginBottom: 12 }}>
        Tu pedido <span style={{ fontWeight: 700, color: "#21a03c" }}>{orderId}</span> ha sido confirmado con éxito.
      </div>
      <div style={{ marginBottom: 18 }}>
        <span style={{ fontWeight: 600, color: "#2356b2" }}>Detalles de Envío</span>
        <div style={{ fontSize: 15, margin: "8px 0 0 0" }}>
          Tu pedido será enviado a la dirección registrada.<br />
          Tiempo estimado de entrega: <span style={{ fontWeight: 700, color: "#228b22" }}>3-5 días hábiles.</span>
        </div>
      </div>
      <div style={{ display: "flex", justifyContent: "center", gap: 15, marginTop: 24 }}>
        <button
          style={{
            background: "#2385e6", color: "#fff", fontWeight: 600,
            border: "none", borderRadius: 5, padding: "10px 20px",
            fontSize: 16, cursor: "pointer"
          }}
          onClick={goToOrders}
        >
          Ver Historial de Pedidos
        </button>
        <button
          style={{
            background: "#28a745", color: "#fff", fontWeight: 600,
            border: "none", borderRadius: 5, padding: "10px 22px",
            fontSize: 16, cursor: "pointer"
          }}
          onClick={goToHome}
        >
          Continuar Comprando
        </button>
      </div>
      {/* Contador visible SOLO en los últimos 3 segundos */}
      {showCountdown && (
        <div style={{
          marginTop: 24,
          color: "#e74c3c",
          fontWeight: 700,
          fontSize: 17,
          letterSpacing: ".03em"
        }}>
          Serás redirigido al menú principal en {secondsLeft}...
        </div>
      )}
    </div>
  )
}
