package com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteCreateDTO(

        @NotBlank(message = "El nombre completo es obligatorio")
        String nombreCompleto,               // nada de @Size aquí (o si quieres, @Size(max=80))

        @NotBlank @Email(message = "Email inválido")
        String email,

        @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank @Size(max = 13, message = "El RFC no puede exceder 13 caracteres")
        String rfc

) {}
