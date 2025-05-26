// src/main/java/.../dto/pedido/PedidoCreateDTO.java
package com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoCreateDTO(
        @NotNull List<ItemDTO> items
) {
    public static record ItemDTO(
            @NotNull Long productoId,
            @Min(1)   Integer cantidad
    ) {}
}
