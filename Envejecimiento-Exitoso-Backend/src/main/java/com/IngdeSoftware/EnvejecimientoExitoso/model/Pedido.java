package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter @Setter @NoArgsConstructor
public class Pedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime fecha = LocalDateTime.now();

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<PedidoItem> getItems() {
        return items;
    }

    public void setItems(List<PedidoItem> items) {
        this.items = items;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.NUEVO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();

    private BigDecimal total;

    /* -------  Fábrica desde Carrito  ------- */
    public static Pedido crearDesdeCarrito(Carrito carrito) {
        Pedido p = new Pedido();
        p.usuario = carrito.getUsuario();
        carrito.getItems().forEach(ci ->
                p.items.add(new PedidoItem(p, ci.getProducto(), ci.getCantidad(), ci.getPrecioUnitario()))
        );
        p.calcularTotal();
        return p;
    }
    private void calcularTotal() {
        total = items.stream()
                .map(PedidoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
