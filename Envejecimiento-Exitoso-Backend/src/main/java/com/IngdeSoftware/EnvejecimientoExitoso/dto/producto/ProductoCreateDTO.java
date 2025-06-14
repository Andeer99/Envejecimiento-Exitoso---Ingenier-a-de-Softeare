package com.IngdeSoftware.EnvejecimientoExitoso.dto.producto;

import java.math.BigDecimal;

public record ProductoCreateDTO(
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imageUrl,
        String categoria
) {}
