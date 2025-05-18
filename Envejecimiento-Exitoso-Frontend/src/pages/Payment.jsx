import React, { useContext, useState } from 'react'
import { CartContext } from '../context/CartContext'
import { useNavigate } from 'react-router-dom'

export default function Payment() {
  const { cart, clearCart } = useContext(CartContext)
  const [metodoPago, setMetodoPago] = useState('tarjeta')
  const [datosPago, setDatosPago] = useState({
    tarjeta: '', expiracion: '', cvv: '', paypal: ''
  })
  const [pagado, setPagado] = useState(false)
  const navigate = useNavigate()

  const total = cart.reduce((sum, p) => sum + (p.cantidad || 0) * (p.precioUnitario || 0), 0)

  function handleInput(e) {
    setDatosPago({ ...datosPago, [e.target.name]: e.target.value })
  }

  function confirmarPago(e) {
    e.preventDefault()
    setPagado(true)
    clearCart()
    navigate('/confirm')
  }

  if (pagado) {
    return (
      <div style={{
        maxWidth: 550, margin: "56px auto",
        background: "#fff", borderRadius: 14,
        boxShadow: "0 2px 16px #0002",
        padding: "46px 30px", textAlign: "center"
      }}>
        <h2 style={{ color: "#28a745", fontWeight: 800, marginBottom: 24 }}>¡Pago realizado con éxito!</h2>
        <div style={{ fontSize: 42, margin: "16px 0 26px 0" }}>✅</div>
        Serás redirigido a la página principal.
      </div>
    )
  }

  return (
    <div style={{
      maxWidth: 600,
      margin: "56px auto",
      padding: "10px 0 30px 0",
      background: "#f4f8fc",
      borderRadius: 18,
      boxShadow: "0 2px 14px #0002"
    }}>
      {/* RESUMEN DEL PEDIDO */}
      <section style={{
        background: "#fff",
        borderRadius: 14,
        boxShadow: "0 1px 6px #0001",
        margin: "30px 38px 28px 38px",
        padding: "28px 30px"
      }}>
        <h2 style={{ fontWeight: 800, fontSize: 22, marginBottom: 20, color: "#214170" }}>Resumen del Pedido</h2>
        <table style={{
          width: "100%",
          borderCollapse: "collapse",
          marginBottom: 20,
          fontSize: 17
        }}>
          <thead>
            <tr style={{ background: "#f8fafb" }}>
              <th style={{ padding: "7px 6px", borderBottom: "1.5px solid #e1e7ef", textAlign: "left" }}>Producto</th>
              <th style={{ padding: "7px 6px", borderBottom: "1.5px solid #e1e7ef", textAlign: "center" }}>Cantidad</th>
              <th style={{ padding: "7px 6px", borderBottom: "1.5px solid #e1e7ef", textAlign: "center" }}>Precio</th>
            </tr>
          </thead>
          <tbody>
            {cart.map(item => (
              <tr key={item.id} style={{ background: "#fafdff" }}>
                <td style={{ padding: "7px 6px" }}>{item.nombreProducto}</td>
                <td style={{ padding: "7px 6px", textAlign: "center" }}>{item.cantidad}</td>
                <td style={{ padding: "7px 6px", textAlign: "center" }}>
                  ${((item.precioUnitario || 0) * (item.cantidad || 0)).toLocaleString("es-MX", { minimumFractionDigits: 2 })}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div style={{ textAlign: "right", fontWeight: 700, fontSize: 18, color: "#2b8246" }}>
          Total: ${total.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
        </div>
      </section>

      {/* MÉTODO DE PAGO */}
      <section style={{
        background: "#fff",
        borderRadius: 14,
        boxShadow: "0 1px 6px #0001",
        margin: "0 38px 28px 38px",
        padding: "28px 30px"
      }}>
        <h3 style={{ fontWeight: 700, marginBottom: 20, color: "#214170" }}>Método de Pago</h3>
        <div style={{ marginBottom: 18 }}>
          <label style={{ marginRight: 38, cursor: "pointer", fontWeight: 500 }}>
            <input
              type="radio"
              name="metodoPago"
              value="tarjeta"
              checked={metodoPago === "tarjeta"}
              onChange={() => setMetodoPago("tarjeta")}
              style={{ marginRight: 8 }}
            />
            <img src="https://cdn-icons-png.flaticon.com/128/196/196578.png" alt="Tarjeta" width={32} style={{ verticalAlign: "middle", marginRight: 5 }} />
            Tarjeta de Crédito
          </label>
          <label style={{ cursor: "pointer", fontWeight: 500 }}>
            <input
              type="radio"
              name="metodoPago"
              value="paypal"
              checked={metodoPago === "paypal"}
              onChange={() => setMetodoPago("paypal")}
              style={{ marginRight: 8 }}
            />
            <img src="https://cdn-icons-png.flaticon.com/128/174/174861.png" alt="PayPal" width={30} style={{ verticalAlign: "middle", marginRight: 5 }} />
            PayPal
          </label>
        </div>

        <form onSubmit={confirmarPago} autoComplete="off">
          <h3 style={{ fontWeight: 700, marginBottom: 18, color: "#214170" }}>Detalles de Pago</h3>
          {metodoPago === "tarjeta" ? (
            <>
              <div>
                <label style={{ fontWeight: 600 }}>Número de Tarjeta</label>
                <input
                  type="text"
                  name="tarjeta"
                  maxLength={19}
                  placeholder="1234 5678 9012 3456"
                  value={datosPago.tarjeta}
                  onChange={handleInput}
                  style={{
                    width: "100%", marginBottom: 12, padding: "9px 7px", borderRadius: 5,
                    border: "1.5px solid #e0e5f3", fontSize: 15
                  }}
                  required
                />
              </div>
              <div style={{ display: "flex", gap: 12 }}>
                <div style={{ flex: 1 }}>
                  <label style={{ fontWeight: 600 }}>Fecha de Expiración</label>
                  <input
                    type="text"
                    name="expiracion"
                    placeholder="MM/AA"
                    maxLength={5}
                    value={datosPago.expiracion}
                    onChange={handleInput}
                    style={{
                      width: "100%", marginBottom: 12, padding: "9px 7px", borderRadius: 5,
                      border: "1.5px solid #e0e5f3", fontSize: 15
                    }}
                    required
                  />
                </div>
                <div style={{ flex: 1 }}>
                  <label style={{ fontWeight: 600 }}>CVV</label>
                  <input
                    type="text"
                    name="cvv"
                    maxLength={4}
                    placeholder="123"
                    value={datosPago.cvv}
                    onChange={handleInput}
                    style={{
                      width: "100%", marginBottom: 12, padding: "9px 7px", borderRadius: 5,
                      border: "1.5px solid #e0e5f3", fontSize: 15
                    }}
                    required
                  />
                </div>
              </div>
            </>
          ) : (
            <div>
              <label style={{ fontWeight: 600 }}>Correo de PayPal</label>
              <input
                type="email"
                name="paypal"
                placeholder="tuemail@paypal.com"
                value={datosPago.paypal}
                onChange={handleInput}
                style={{
                  width: "100%", marginBottom: 12, padding: "9px 7px", borderRadius: 5,
                  border: "1.5px solid #e0e5f3", fontSize: 15
                }}
                required
              />
            </div>
          )}
          <button type="submit" style={{
            width: "100%",
            marginTop: 16,
            background: "#28a745",
            color: "#fff",
            border: "none",
            borderRadius: 5,
            padding: "16px 0",
            fontWeight: 800,
            fontSize: "1.13em",
            cursor: "pointer",
            letterSpacing: ".03em"
          }}>
            Confirmar Pago
          </button>
        </form>
      </section>
    </div>
  )
}
