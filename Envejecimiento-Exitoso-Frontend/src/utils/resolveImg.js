/**
 * Devuelve una URL lista para <img src="…">
 *  - Si es absoluta (http/https) ⇒ se deja tal cual
 *  - Si empieza por /uploads o uploads ⇒ prefiere VITE_API_URL
 *  - BONUS: si viene por error con "/api/uploads" lo corregimos
 */
export function resolveImg(u) {
  if (!u) return "/placeholder.png";

  // absoluta
  if (/^https?:\/\//i.test(u)) return u;

  // corrige paths antiguos
  if (u.startsWith("/api/uploads")) u = u.replace("/api/uploads", "/uploads");

  const api  = import.meta.env.VITE_API_URL.replace(/\/+$/, "");
  const path = u.startsWith("/") ? u : `/${u}`;

  return `${api}${path}`;
}
