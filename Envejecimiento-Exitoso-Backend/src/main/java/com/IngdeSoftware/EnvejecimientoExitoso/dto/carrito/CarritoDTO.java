package com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito;


import java.math.BigDecimal;
import java.util.List;

public record CarritoDTO(
        Long                id,
        List<CarritoItemDTO> items,
        BigDecimal total
) {}