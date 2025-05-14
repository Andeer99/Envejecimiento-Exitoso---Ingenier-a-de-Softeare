package com.IngdeSoftware.EnvejecimientoExitoso.dto.producto;

import java.math.BigDecimal;

public record ProductoDTO(
        Long       id,
        String     nombre,
        String     descripcion,
        BigDecimal precio,
        Integer    stock,
        String     categoria
) {}
