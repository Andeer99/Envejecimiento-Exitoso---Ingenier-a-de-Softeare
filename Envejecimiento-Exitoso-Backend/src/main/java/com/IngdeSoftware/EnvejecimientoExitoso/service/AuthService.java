package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.AuthResponse;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.LoginRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.auth.RefreshRequest;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Role;
import com.IngdeSoftware.EnvejecimientoExitoso.model.RoleName;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil               jwtUtil;
    private final UsuarioRepository     usuarioRepo;
    private final UsuarioDetailsService uds;

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

        return new AuthResponse(access, refresh, resolveMainRole(u).name());
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

        return new AuthResponse(access, refresh, resolveMainRole(u).name());
    }

    /* ---------- Helper ---------- */
    /**
     * Devuelve el rol “principal” de un usuario.
     *  - Prioriza ADMIN; si no, regresa el primero encontrado o CLIENTE por defecto.
     */
    private RoleName resolveMainRole(Usuario u) {
        return u.getRoles().stream()
                .map(Role::getName)                 // RoleName
                .sorted((a, b) ->                  // ADMIN primero
                        a == RoleName.ADMIN ? -1 :
                                b == RoleName.ADMIN ?  1 : 0)
                .findFirst()
                .orElse(RoleName.CLIENTE);
    }
}
