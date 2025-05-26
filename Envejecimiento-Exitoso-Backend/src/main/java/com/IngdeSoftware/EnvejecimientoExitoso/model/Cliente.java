package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Subtipo de Usuario que representa a un cliente del sistema.
 */
@Entity
@DiscriminatorValue("CLIENTE")                //-- Estrategia SINGLE_TABLE (asumida en Usuario)
@Getter @Setter @ToString(callSuper = true)
public class Cliente extends Usuario {

    @Column(length = 13, nullable = true, unique = true)   // 13 dígitos para RFC personas físicas
    @NotBlank(message = "El RFC es obligatorio")
    private String rfc;

    /*  --- constructores auxiliares ---  */
    public Cliente() { }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Cliente(String rfc) {
        this.rfc = rfc;
    }
}
