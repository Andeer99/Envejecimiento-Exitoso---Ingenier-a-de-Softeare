package com.IngdeSoftware.EnvejecimientoExitoso.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank String refreshToken
) {}
