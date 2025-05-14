package com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente;

/** DTO simple para exponer datos de cliente en listados */
public record ClienteDTO(
        Long   id,
        String nombreCompleto,
        String email,
        String rfc
) {}
