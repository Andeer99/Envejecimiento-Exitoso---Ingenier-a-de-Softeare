package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    /* ---------- Crear pedido desde carrito (o desde DTO) ---------- */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO crearPedido(
            Authentication auth,
            @Valid @RequestBody PedidoCreateDTO pedidoCreateDTO
    ) {
        return service.createOrderFromDto(auth.getName(), pedidoCreateDTO);
    }

    /* ---------- Mis pedidos ---------- */
    @GetMapping("/mios")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDTO> misPedidos(Authentication auth) {
        return service.findOrdersByUser(auth.getName());
    }

    /* ---------- Detalle (admin o dueño) ---------- */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoSecurity.esPropietario(#id, authentication.name)")
    public PedidoDTO detalle(@PathVariable Long id) {
        return service.findByIdDTO(id);
    }

    /** Cancela (no borra) un pedido cambiando su estado a CANCELADO */
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    @ResponseStatus(HttpStatus.OK)
    public PedidoDTO cancelarPedido(
            @PathVariable Long id,
            Authentication auth
    ) {
        return service.cancelarPedido(id, auth.getName());
    }
}
