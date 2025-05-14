package com.IngdeSoftware.EnvejecimientoExitoso.dto.auth;

/** Respuesta común (login o refresh) */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String role       /* rol principal del usuario */
) {}
