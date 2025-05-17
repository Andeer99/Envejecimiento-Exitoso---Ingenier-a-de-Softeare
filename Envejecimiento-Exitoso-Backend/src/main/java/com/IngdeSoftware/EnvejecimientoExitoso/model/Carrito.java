package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@Getter @Setter @NoArgsConstructor
public class Carrito {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* OneToOne con Usuario (un carrito “activo” por usuario) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    /* ----------  Lógica de dominio  ---------- */
    public void agregar(Producto producto, int cantidad) {
        items.add(new CarritoItem(this, producto, cantidad, producto.getPrecio()));
    }
    public void actualizarCantidad(Long itemId, int cantidad) {
        items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .ifPresent(i -> i.setCantidad(cantidad));
    }
    public void eliminarItem(Long itemId) {
        items.removeIf(i -> i.getId().equals(itemId));
    }
    public void vaciar() { items.clear(); }
    public boolean isEmpty() { return items.isEmpty(); }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }

    @Transient
    public BigDecimal getTotal() {
        return items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
