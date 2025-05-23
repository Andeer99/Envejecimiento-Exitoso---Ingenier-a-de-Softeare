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
    @Column(name= "pasarela_id", unique = true, length = 64)
    private String pasarelaId;

    private LocalDateTime fecha = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getPasarelaPayload() {
        return pasarelaPayload;
    }

    public void setPasarelaPayload(String pasarelaPayload) {
        this.pasarelaPayload = pasarelaPayload;
    }

    public String getPasarelaId() {
        return pasarelaId;
    }

    public void setPasarelaId(String pasarelaId) {
        this.pasarelaId = pasarelaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

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
