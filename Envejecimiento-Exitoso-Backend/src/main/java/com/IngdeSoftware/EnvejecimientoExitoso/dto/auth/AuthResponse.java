package com.IngdeSoftware.EnvejecimientoExitoso.dto.auth;

/** Petición de login */
public record LoginRequest(String email, String password) {}

/** Petición de renovación de token */
public record RefreshRequest(String refreshToken) {}

/** Respuesta común (login o refresh) */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String role       /* rol principal del usuario */
) {}
