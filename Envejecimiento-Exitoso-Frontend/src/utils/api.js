// utils/api.js
export async function fetchWithAuth(url, options = {}) {
  const token = localStorage.getItem('accessToken');
  let res = await fetch(url, {
    ...options,
    headers: {
      'Content-Type':'application/json',
      'Authorization': `Bearer ${token}`,
      ...options.headers
    }
  });

  // Si expiró → refrescamos
  if (res.status === 401) {
    // Llamada al refresh-token
    const refreshToken = localStorage.getItem('refreshToken');
    const r = await fetch(`${VITE_API_URL}/api/auth/refresh`, {
      method:'POST',
      headers:{ 'Authorization': `Bearer ${refreshToken}` }
    });
    if (r.ok) {
      const { accessToken:newToken } = await r.json();
      localStorage.setItem('accessToken', newToken);

      // Reintentar la petición original
      res = await fetch(url, {
        ...options,
        headers:{
          'Content-Type':'application/json',
          'Authorization': `Bearer ${newToken}`,
          ...options.headers
        }
      });
    }
  }
  return res;
}
