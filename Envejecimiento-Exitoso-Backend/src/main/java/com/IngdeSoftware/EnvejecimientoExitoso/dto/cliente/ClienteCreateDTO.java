package com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteCreateDTO(
        @NotBlank String nombreCompleto,
        @NotBlank @Email     String email,
        @NotBlank @Size(min=8) String password,
        @NotBlank            String rfc
) {}
