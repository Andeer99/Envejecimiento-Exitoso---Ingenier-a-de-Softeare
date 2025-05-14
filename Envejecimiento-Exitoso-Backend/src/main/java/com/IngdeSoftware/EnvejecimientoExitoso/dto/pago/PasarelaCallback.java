package com.IngdeSoftware.EnvejecimientoExitoso.dto.pago;

/** Payload que la pasarela de pago nos envía al webhook /confirmacion */
public record PasarelaCallback(
        String transaccionId,
        String status,           // “exitoso”, “fallido”, “pendiente”, etc.
        String firma             // para verificar integridad, si aplica
) {}