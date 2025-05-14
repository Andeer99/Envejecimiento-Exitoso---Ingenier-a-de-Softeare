package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PedidoItem {

    public PedidoItem(Pedido p, Producto prod, int cant, BigDecimal precio) {
        this.pedido = p;
        this.producto = prod;
        this.cantidad = cant;
        this.precioUnitario = precio;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    @Transient
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
