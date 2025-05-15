package com.IngdeSoftware.EnvejecimientoExitoso.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY      = "tu-clave-secreta-aquí";
    private static final long   ACCESS_EXP_MS   = 1000 * 60 * 15;   // 15 minutos
    private static final long   REFRESH_EXP_MS  = 1000L * 60 * 60 * 24 * 7; // 7 días

    // Genera access token
    public String generateToken(UserDetails user) {
        return buildToken(user.getUsername(), ACCESS_EXP_MS);
    }

    // Genera refresh token
    public String generateRefreshToken(UserDetails user) {
        return buildToken(user.getUsername(), REFRESH_EXP_MS);
    }

    private String buildToken(String subject, long expirationMs) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Extrae el username (subject)
    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    // Valida el token contra un UserDetails
    public boolean validateToken(String token, UserDetails user) {
        String username = getUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    // Para el refresh: sólo comprueba expiración
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
