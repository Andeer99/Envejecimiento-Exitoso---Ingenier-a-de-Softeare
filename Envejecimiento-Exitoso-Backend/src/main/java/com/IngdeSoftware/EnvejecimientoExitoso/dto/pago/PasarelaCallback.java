package com.IngdeSoftware.EnvejecimientoExitoso.dto.pago;

/**
 * DTO que recibe tu aplicación desde la pasarela
 */
public record PasarelaCallback(
        Long transaccionId,
        String status,
        String firma
) {}
