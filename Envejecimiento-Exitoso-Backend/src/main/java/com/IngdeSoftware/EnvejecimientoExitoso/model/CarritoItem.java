package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "carrito_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CarritoItem {

    public CarritoItem(Carrito carrito, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "producto_id")
    private Producto producto;

    @Min(1) @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal precioUnitario;

    /* subtotal = cantidad * precioUnitario */
    @Transient
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
