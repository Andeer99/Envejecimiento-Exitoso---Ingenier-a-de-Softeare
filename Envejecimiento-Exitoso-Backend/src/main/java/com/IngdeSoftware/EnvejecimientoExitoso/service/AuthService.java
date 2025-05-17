package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.AuthResponse;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.LoginRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.RefreshRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil               jwtUtil;
    private final UsuarioRepository     usuarioRepo;
    private final UsuarioDetailsService uds;

    public AuthService(AuthenticationManager authManager,
                       JwtUtil jwtUtil,
                       UsuarioRepository usuarioRepo,
                       UsuarioDetailsService uds) {
        this.authManager  = authManager;
        this.jwtUtil      = jwtUtil;
        this.usuarioRepo  = usuarioRepo;
        this.uds          = uds;
    }

    /* ---------- Login ---------- */
    public AuthResponse authenticate(LoginRequest req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails ud  = uds.loadUserByUsername(req.email());
        String access  = jwtUtil.generateToken(ud);
        String refresh = jwtUtil.generateRefreshToken(ud);

        Usuario u = usuarioRepo.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        return new AuthResponse(access, refresh, u.getMainRole());
    }

    /* ---------- Refresh ---------- */
    public AuthResponse refreshToken(RefreshRequest req) {

        if (!jwtUtil.validateToken(req.refreshToken()))
            throw new IllegalArgumentException("Refresh inválido o expirado");

        String email = jwtUtil.getUsername(req.refreshToken());
        UserDetails ud = uds.loadUserByUsername(email);

        String access  = jwtUtil.generateToken(ud);
        String refresh = jwtUtil.generateRefreshToken(ud);

        Usuario u = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        return new AuthResponse(access, refresh, u.getMainRole());
    }
}
