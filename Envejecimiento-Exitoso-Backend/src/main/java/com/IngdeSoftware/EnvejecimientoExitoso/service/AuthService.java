package com.IngdeSoftware.EnvejecimientoExitoso.service;


import com.IngdeSoftware.EnvejecimientoExitoso.config.JwtUtil;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.AuthResponse;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.LoginRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.RefreshRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwt;                        // helper que ya registraste en Config
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public AuthResponse authenticate(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        Usuario user = repo.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        String access  = jwt.generateToken(user);
        String refresh = jwt.generateRefreshToken(user);

        return new AuthResponse(access, refresh, user.getMainRole());
    }

    public AuthResponse refreshToken(RefreshRequest req) {
        if (!jwt.validateToken(req.refreshToken())) {
            throw new IllegalArgumentException("Refresh inválido");
        }
        String email = jwt.getUsername(req.refreshToken());
        Usuario user = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        String access  = jwt.generateToken(user);
        String refresh = jwt.generateRefreshToken(user);
        return new AuthResponse(access, refresh, user.getMainRole());
    }
}