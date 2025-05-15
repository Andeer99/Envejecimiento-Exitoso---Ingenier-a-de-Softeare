package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter @Setter @NoArgsConstructor
public class Pago {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    /* id devuelto por la pasarela externa */
    @Column(unique = true, length = 64)
    private String pasarelaId;

    private LocalDateTime fecha = LocalDateTime.now();

    /* ------------- helpers de dominio ------------- */
    public static Pago fromPedido(Pedido p) {
        Pago pago = new Pago();
        pago.pedido = p;
        pago.monto  = p.getTotal();
        return pago;
    }
    public void actualizarEstado(String s) {
        this.estado = EstadoPago.valueOf(s.toUpperCase());
    }

    @Column(columnDefinition = "TEXT")
    private String pasarelaPayload;

}
