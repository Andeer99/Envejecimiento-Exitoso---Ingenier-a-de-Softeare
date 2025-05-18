// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { resolve } from 'path'

export default defineConfig({
  // 1) Carpeta raíz de tu código fuente
  root: resolve(__dirname, 'src'),

  // 2) Para que Vite deje la caché fuera de node_modules
  cacheDir: resolve(__dirname, '.vite'),

  // 3) URL base en producción
  base: '/',

  // 4) Dev server
  server: {
    port: 3000,
    // evita problemas de OneDrive locking
    fs: {
      strict: false
    }
  },

  // 5) Build output
  build: {
    outDir: resolve(__dirname, 'dist'),
    emptyOutDir: true,
    sourcemap: true, // opcional
  },

  // 6) Plugins
  plugins: [
    react()
  ]
})
