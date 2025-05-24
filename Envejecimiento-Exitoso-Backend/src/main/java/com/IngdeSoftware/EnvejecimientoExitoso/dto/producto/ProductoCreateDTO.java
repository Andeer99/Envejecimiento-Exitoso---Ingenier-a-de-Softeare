package com.IngdeSoftware.EnvejecimientoExitoso.dto.producto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoCreateDTO(
        @NotBlank           String     nombre,
        @Size(max = 255)    String     descripcion,
        @DecimalMin("0.0")  BigDecimal precio,
        @Min(0)             Integer    stock,
        @NotBlank           String     imageUrl,
        @Size(max = 50)     String     categoria

) {}
