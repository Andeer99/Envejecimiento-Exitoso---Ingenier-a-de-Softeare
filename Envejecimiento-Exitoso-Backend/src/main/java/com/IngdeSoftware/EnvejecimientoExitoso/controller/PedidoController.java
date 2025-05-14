package com.IngdeSoftware.EnvejecimientoExitoso.controller;


import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    /** Crear pedido a partir del carrito del usuario */
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO crearPedido(Authentication auth) {
        return service.createOrder(auth.getName());
    }

    /** Pedidos del cliente actual */
    @GetMapping("/mios")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDTO> misPedidos(Authentication auth) {
        return service.findOrdersByUser(auth.getName());
    }

    /** Detalle de pedido (admin o dueño) */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoSecurity.esPropietario(#id, authentication.name)")
    public PedidoDTO detalle(@PathVariable Long id) {
        return service.findByIdDTO(id);
    }
}
