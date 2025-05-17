package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.AuthResponse;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.LoginRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.RefreshRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /* ---------- Login ---------- */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return service.authenticate(request);
    }

    /* ---------- Refresh ---------- */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return service.refreshToken(request);
    }
}
