package com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente;

public record ClienteResponseDTO(
        Long   id,
        String nombreCompleto,
        String email,
        String rfc
) {}
