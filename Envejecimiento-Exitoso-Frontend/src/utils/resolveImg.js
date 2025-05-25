// src/utils/resolveImg.js

/**
 * Devuelve una URL lista para <img src="…">:
 *   • /uploads/…  →  la dejamos tal cual (se sirve desde el frontend)
 *   • http(s)://… →  la dejamos tal cual (CDN, S3…)
 *   • cualquier otra ruta relativa → la anteponemos a la API
 */
export function resolveImg(u) {
  if (!u) return "/placeholder.png";              // imagen genérica

  // 1) URL absoluta (https://…)
  if (/^https?:\/\//i.test(u)) return u;

  // 2) Imágenes estáticas guardadas en el propio frontend
  if (u.startsWith("/uploads/") || u.startsWith("uploads/"))
    return u.startsWith("/") ? u : `/${u}`;

  // 3) Rutas que realmente vive el backend (/uploads en servidor, /files, etc.)
  const api = import.meta.env.VITE_API_URL.replace(/\/+$/, "");
  const path = u.startsWith("/") ? u : `/${u}`;
  return `${api}${path}`;
}
