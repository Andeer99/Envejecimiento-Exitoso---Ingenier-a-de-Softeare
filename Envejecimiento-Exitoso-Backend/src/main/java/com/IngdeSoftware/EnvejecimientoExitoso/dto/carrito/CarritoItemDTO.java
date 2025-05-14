package com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito;
import java.math.BigDecimal;


public record CarritoItemDTO(
        Long       id,
        Long       productoId,
        String     nombreProducto,
        Integer    cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}