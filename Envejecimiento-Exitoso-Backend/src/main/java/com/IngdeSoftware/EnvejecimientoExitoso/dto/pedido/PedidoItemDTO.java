package com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido;

import java.math.BigDecimal;

public record PedidoItemDTO(
        Long       id,
        Long       productoId,
        String     nombreProducto,
        Integer    cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}