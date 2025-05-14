package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false, length = 80)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @DecimalMin("0.0") @Digits(integer = 9, fraction = 2)
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal precio;

    @Min(0) @Column(nullable = false)
    private Integer stock;

    @Column(length = 50)
    private String categoria;
}
