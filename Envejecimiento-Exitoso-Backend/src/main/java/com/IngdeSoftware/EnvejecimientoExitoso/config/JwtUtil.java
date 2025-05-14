package com.IngdeSoftware.EnvejecimientoExitoso.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")               //  env o application.properties
    private String secret;

    @Value("${jwt.expiration-ms:3600000}") // default 1 h
    private long expiration;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /* ----------  Generate  ---------- */
    public String generateToken(Usuario u) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("role", u.getRoles().iterator().next())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(Usuario u) {
        Date now = new Date();
        long refreshExp = expiration * 24;            // 24 h por ejemplo
        return Jwts.builder()
                .setSubject(u.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExp))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ----------  Validate  ---------- */
    public boolean validateToken(String token) {
        try { parse(token); return true; }
        catch (JwtException | IllegalArgumentException e) { return false; }
    }
    public String getUsername(String token) {
        return parse(token).getBody().getSubject();
    }
    /* ----------  Interno  ---------- */
    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }
}
