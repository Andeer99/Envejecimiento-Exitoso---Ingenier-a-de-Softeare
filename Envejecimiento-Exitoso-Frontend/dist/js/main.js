// main.js

function appState() {
  return {
    // 1) Ajusta este endpoint al dominio donde corre tu API
    apiBase: "http://localhost:8080/api",
    
    // 2) Estado reactivo
    accessToken: localStorage.getItem("accessToken") || null,
    refreshToken: localStorage.getItem("refreshToken") || null,
    isAuthenticated: !!localStorage.getItem("accessToken"),
    loginEmail: "",
    loginPassword: "",
    productos: [],

    // 3) Helper genérico para fetch
    async apiFetch(path, options = {}) {
      const headers = {
        "Content-Type": "application/json",
        ...(this.accessToken && { Authorization: `Bearer ${this.accessToken}` })
      };
      const res = await fetch(`${this.apiBase}/${path}`, {
        headers,
        ...options
      });
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      return res.status === 204 ? null : await res.json();
    },

    // 4) Login: guarda tokens y carga productos
    async login() {
      try {
        const data = await this.apiFetch("auth/login", {
          method: "POST",
          body: JSON.stringify({
            email: this.loginEmail,
            password: this.loginPassword
          })
        });
        this.accessToken  = data.accessToken;
        this.refreshToken = data.refreshToken;
        localStorage.setItem("accessToken",  data.accessToken);
        localStorage.setItem("refreshToken", data.refreshToken);
        this.isAuthenticated = true;
        await this.loadProducts();
      } catch (e) {
        console.error(e);
        alert("Login fallido");
      }
    },

    // 5) Carga la lista de productos desde la API
    async loadProducts() {
      try {
        this.productos = await this.apiFetch("producto");
      } catch (e) {
        console.error(e);
      }
    },

    // 6) Añadir un ítem al carrito
    async addToCart(productoId) {
      try {
        await this.apiFetch("carrito/items", {
          method: "POST",
          body: JSON.stringify({ productoId, cantidad: 1 })
        });
        alert("Producto agregado al carrito");
      } catch (e) {
        console.error(e);
        alert("Error al agregar al carrito");
      }
    }
  };
}
