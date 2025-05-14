package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Transaccion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;                 // EJ: “CREACION_PEDIDO”, “CAMBIO_ESTADO_PAGO”

    private String referencia;           // id de pedido/pago afectado

    private String detalle;              // JSON o texto libre

    private LocalDateTime fecha = LocalDateTime.now();
}
