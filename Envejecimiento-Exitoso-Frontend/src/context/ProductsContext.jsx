// src/context/ProductsContext.jsx
import React, {
  createContext,
  useCallback,
  useEffect,
  useState
} from "react"
import { fetchAuth } from "../utils/fetchAuth"
export const ProductsContext = createContext()

export function ProductsProvider({ children }) {
  const [products, setProducts] = useState([])
  const apiBase = import.meta.env.VITE_API_URL

  /* ------------------- helpers ------------------- */
  const fetchProducts = useCallback(() => {
    return fetchAuth(`${apiBase}/api/productos`)
      .then(r => r.json())
      .then(setProducts)
      .catch(console.error)
  }, [apiBase])

  const deleteProduct = id =>
    fetchAuth(`${apiBase}/api/productos/${id}`, { method: "DELETE" })
      .then(() => setProducts(prev => prev.filter(p => p.id !== id)))

  const updateProduct = p =>
    fetchAuth(`${apiBase}/api/productos/${p.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(p)
    }).then(() => setProducts(prev => prev.map(it => (it.id === p.id ? p : it))))

  const addProduct = p =>
    fetchAuth(`${apiBase}/api/productos`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(p)
    })
      .then(r => r.json())
      .then(newP => setProducts(prev => [...prev, newP]))

  /* ------------------- carga inicial -------------- */
  useEffect(() => {
    // encapsulamos para NO devolver la promise
    fetchProducts()
  }, [fetchProducts])

  
  /* ------------------- contexto ------------------- */
  return (
    <ProductsContext.Provider
      value={{ products, fetchProducts, deleteProduct, updateProduct, addProduct }}
    >
      {children}
    </ProductsContext.Provider>
  )
}
