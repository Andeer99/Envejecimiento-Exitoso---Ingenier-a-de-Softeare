package com.IngdeSoftware.EnvejecimientoExitoso.dto.auth;

/** Petición de renovación de token */
public record RefreshRequest(String refreshToken) {}