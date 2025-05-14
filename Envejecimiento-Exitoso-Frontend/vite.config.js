// vite.config.js
import { defineConfig } from 'vite'
import { resolve } from 'path'

export default defineConfig({
  // 1) Raíz del front
  root: 'src',
  base: './',
  publicDir: false,
  build: {
    // Al nivel de tu carpeta de front:
    outDir: resolve(__dirname, 'dist'),
    emptyOutDir: true,

    // 5) Multi-page: explícito cada HTML de entrada
    rollupOptions: {
      input: {
        index:    resolve(__dirname, 'src/index.html'),
        login:    resolve(__dirname, 'src/login.html'),
        catalog:  resolve(__dirname, 'src/catalog.html'),
        cart:     resolve(__dirname, 'src/cart.html'),
        confirm:  resolve(__dirname, 'src/confirm.html'),
        registro: resolve(__dirname, 'src/registro.html'),
        payment:  resolve(__dirname, 'src/payment.html'),
      },
       output: {
    entryFileNames:  'js/[name].js',
    chunkFileNames:  'js/[name]-[hash].js',
    assetFileNames: assetInfo => {
      if (assetInfo.name.endsWith('.css')) return 'css/[name].css'
      return 'assets/[name][extname]'
    }
    }
  },

  server: {
    port: 3000
  }
}})
