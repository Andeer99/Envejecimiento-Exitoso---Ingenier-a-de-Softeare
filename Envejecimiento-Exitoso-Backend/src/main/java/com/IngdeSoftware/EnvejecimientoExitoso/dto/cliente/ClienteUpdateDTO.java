package com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solo permitimos cambiar nombreCompleto y password.
 * Email y RFC no se tocan en el update.
 */
public record ClienteUpdateDTO(
        @NotBlank String nombreCompleto,
        @NotBlank @Size(min=8) String password
) {}
