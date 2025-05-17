package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal precioUnitario;

    public CarritoItem() {
    }

    public CarritoItem(Carrito carrito, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Transient
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
