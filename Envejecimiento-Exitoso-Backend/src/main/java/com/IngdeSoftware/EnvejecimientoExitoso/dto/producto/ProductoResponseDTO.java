package com.IngdeSoftware.EnvejecimientoExitoso.dto.producto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        String imageUrl,
        String categoria
) {}