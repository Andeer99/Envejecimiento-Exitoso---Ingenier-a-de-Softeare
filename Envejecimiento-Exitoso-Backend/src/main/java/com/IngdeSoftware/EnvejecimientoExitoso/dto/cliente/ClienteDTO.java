package com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente;

public record
ClienteDTO(
        Long   id,
        String nombreCompleto,
        String password,
        String email,
        String rfc
) {}
