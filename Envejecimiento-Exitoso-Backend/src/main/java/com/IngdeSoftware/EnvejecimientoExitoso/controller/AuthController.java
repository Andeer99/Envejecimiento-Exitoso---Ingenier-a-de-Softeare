package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.AuthResponse;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.LoginRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.RefreshRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;        // genera y valida JWT

    /** Login: devuelve JWT + datos básicos del usuario */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.authenticate(req);
    }

    /** Refresh token */
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest req) {
        return authService.refreshToken(req);
    }

}
