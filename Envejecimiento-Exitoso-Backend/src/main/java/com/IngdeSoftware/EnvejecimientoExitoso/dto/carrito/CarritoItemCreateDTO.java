package com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CarritoItemCreateDTO(
        @NotNull(message = "productoId es obligatorio")
        Long productoId,

        @Min(value = 1, message = "La cantidad mínima es 1")
        int cantidad
) {}
