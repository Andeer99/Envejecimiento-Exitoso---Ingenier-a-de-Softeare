package com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record PedidoDTO(
        Long               id,
        LocalDateTime fecha,
        String             estado,          // NUEVO, PAGADO, …
        List<PedidoItemDTO> items,
        BigDecimal total
) {}
