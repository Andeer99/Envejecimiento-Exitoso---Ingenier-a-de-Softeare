package com.IngdeSoftware.EnvejecimientoExitoso.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY     = "UnaClaveMuyLargaQueSupereLos32CaracteresParaHS256!";
    private static final long   ACCESS_EXP_MS  = 1000L * 60 * 60;     // 1 hora
    private static final long   REFRESH_EXP_MS = 1000L * 60 * 60 * 24; // 24 horas

    private SecretKey signingKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** Genera un access token */
    public String generateToken(UserDetails user) {
        return buildToken(user.getUsername(), ACCESS_EXP_MS);
    }

    /** Genera un refresh token */
    public String generateRefreshToken(UserDetails user) {
        return buildToken(user.getUsername(), REFRESH_EXP_MS);
    }

    private String buildToken(String subject, long expMs) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMs))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrae el username (subject) */
    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    /** Valida un access token contra UserDetails */
    public boolean validateToken(String token, UserDetails user) {
        Claims claims = getAllClaims(token);
        return claims.getSubject().equals(user.getUsername())
                && !claims.getExpiration().before(new Date());
    }

    /** Valida un refresh (solo expiración) */
    public boolean validateToken(String token) {
        return !getAllClaims(token).getExpiration().before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
