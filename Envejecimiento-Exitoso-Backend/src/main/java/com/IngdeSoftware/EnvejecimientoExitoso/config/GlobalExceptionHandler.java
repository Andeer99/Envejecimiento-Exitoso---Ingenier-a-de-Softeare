package com.IngdeSoftware.EnvejecimientoExitoso.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.JwtException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* 401 – credenciales erróneas */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String,String>> authError(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error","Credenciales inválidas"));
    }

    /* 401 – token inválido */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String,String>> jwtError(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error","Token inválido"));
    }

    /* 409 – e-mail duplicado u otra violación */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> integrityError(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause().getMessage().contains("usuarios.email")
                ? "El correo ya está registrado"
                : "Violación de integridad de datos";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", msg));
    }
}
