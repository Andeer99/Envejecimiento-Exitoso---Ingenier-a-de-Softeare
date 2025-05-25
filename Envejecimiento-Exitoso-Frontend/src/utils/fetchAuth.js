// src/utils/fetchAuth.js
export function fetchAuth(url, options = {}) {
  const token = localStorage.getItem("accessToken")
  const headers = {
    ...options.headers,
    ...(token && { Authorization: `Bearer ${token}` }),
    "Content-Type": "application/json"
  }

  return fetch(url, { ...options, headers })
}
