package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.AuthResponse;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.LoginRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.RefreshRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.security.JwtUtil;
import com.IngdeSoftware.EnvejecimientoExitoso.service.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager    authManager;
    private final JwtUtil                  jwtUtil;
    private final UsuarioRepository        repo;

    public AuthService(AuthenticationManager authManager, JwtUtil jwtUtil, UsuarioRepository repo, UsuarioDetailsService uds) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.repo = repo;
        this.uds = uds;
    }

    private final UsuarioDetailsService    uds;

    public AuthResponse authenticate(LoginRequest req) {
        // 1) Autenticar credenciales
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        // 2) Cargar UserDetails para generar el JWT
        UserDetails userDetails = uds.loadUserByUsername(req.email());

        // 3) Generar tokens
        String access  = jwtUtil.generateToken(userDetails);
        String refresh = jwtUtil.generateRefreshToken(userDetails);

        // 4) Devolver roles, tokens…
        // userDetails.getUsername() es el email
        Usuario usuario = repo.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        return new AuthResponse(access, refresh, usuario.getMainRole());
    }

    public AuthResponse refreshToken(RefreshRequest req) {
        // 1) Validar formato y expiración del refreshToken
        if (!jwtUtil.validateToken(req.refreshToken())) {
            throw new IllegalArgumentException("Refresh inválido o expirado");
        }

        // 2) Extraer email y recargar UserDetails
        String email = jwtUtil.getUsername(req.refreshToken());
        UserDetails userDetails = uds.loadUserByUsername(email);

        // 3) Generar nuevo access + refresh
        String access  = jwtUtil.generateToken(userDetails);
        String refresh = jwtUtil.generateRefreshToken(userDetails);

        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        return new AuthResponse(access, refresh, usuario.getMainRole());
    }
}
