package com.IngdeSoftware.EnvejecimientoExitoso.dto.pago;

/**
 * DTO que tu servicio devuelve al frontend tras iniciar el pago
 */
public record PagoInitResponse(
        Long pagoId,
        String redirectUrl
) {}
