package com.IngdeSoftware.EnvejecimientoExitoso.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import io.jsonwebtoken.JwtException;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manejo global de excepciones para la capa web.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*------------------------------------------------------------
     *  Exceptions de VALIDACIÓN / FORMATO (400)
     *-----------------------------------------------------------*/

    /** DTO sin coerción válida / JSON malformado */
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> unreadableBody(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Cuerpo de la petición inválido", ex, req);
    }

    /** Bean Validation en @RequestBody fallida  */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> bodyValidation(MethodArgumentNotValidException ex,
                                                   HttpServletRequest req) {

        // lista “campo : mensaje”
        Map<String,String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (m1, m2) -> m1          // merge   (same key)
                ));

        return build(HttpStatus.BAD_REQUEST, "Validación fallida", ex, req, errors);
    }

    /** Bean Validation en @PathVariable / @RequestParam  */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> constraintViolation(ConstraintViolationException ex,
                                                        HttpServletRequest req) {

        Map<String,String> details = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage(),
                        (m1,m2)-> m1));
        return build(HttpStatus.BAD_REQUEST, "Parámetros inválidos", ex, req, details);
    }

    /*------------------------------------------------------------
     *  Excepciones de AUTORIZACIÓN / AUTENTICACIÓN
     *-----------------------------------------------------------*/

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> authError(AuthenticationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex, req);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> jwtError(JwtException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Token JWT inválido o expirado", ex, req);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiError> accessDenied(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Acceso denegado", ex, req);
    }



    /*------------------------------------------------------------
     *  Excepciones de NEGOCIO / PERSISTENCIA
     *-----------------------------------------------------------*/

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> notFound(EntityNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), ex, req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integrity(DataIntegrityViolationException ex,
                                              HttpServletRequest req) {

        String msg = "Violación de integridad de datos";
        if (ex.getMostSpecificCause().getMessage().contains("usuarios.email"))
            msg = "El correo ya está registrado";

        return build(HttpStatus.CONFLICT, msg, ex, req);
    }

    /*------------------------------------------------------------
     *  Otros errores comunes
     *-----------------------------------------------------------*/

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> methodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                     HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no permitido", ex, req);
    }

    /*------------------------------------------------------------
     *  Fallback – Error inesperado (500)
     *-----------------------------------------------------------*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", ex, req);
    }

    /*------------------------------------------------------------
     *  Helpers
     *-----------------------------------------------------------*/
    private ResponseEntity<ApiError> build(HttpStatus status,
                                           String message,
                                           Exception ex,
                                           HttpServletRequest req) {
        return build(status, message, ex, req, null);
    }

    private ResponseEntity<ApiError> build(HttpStatus status,
                                           String message,
                                           Exception ex,
                                           HttpServletRequest req,
                                           Map<String,String> validationErrors) {

        ApiError body = new ApiError(status, message, req.getRequestURI(), validationErrors);
        // --> aquí se puede loguear si se desea
        return ResponseEntity.status(status).body(body);
    }

    /*------------------------------------------------------------
     *  DTO de error
     *-----------------------------------------------------------*/
    @Getter @ToString
    private static class ApiError {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private final OffsetDateTime timestamp = OffsetDateTime.now();
        private final int            status;
        private final String         error;
        private final String         message;
        private final String         path;
        private final Map<String,String> validation;

        ApiError(HttpStatus status, String message,
                 String path, Map<String,String> validation) {
            this.status     = status.value();
            this.error      = status.getReasonPhrase();
            this.message    = message;
            this.path       = path;
            this.validation = validation;
        }
    }
}
