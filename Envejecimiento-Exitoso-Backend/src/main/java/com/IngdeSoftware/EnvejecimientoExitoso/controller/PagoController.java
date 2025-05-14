package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENTE')")
public class PagoController {

    private final PagoService service;

    /** Iniciar pago para un pedido */
    @PostMapping("/iniciar")
    public PagoInitResponse iniciarPago(@RequestParam Long pedidoId,
                                        Authentication auth) {
        return service.initPayment(pedidoId, auth.getName());
    }

    /** Endpoint que la pasarela externa llamará para confirmar */
    @PostMapping("/confirmacion")
    @CrossOrigin(origins = "${pasarela.callback.origin}")   // permite IP de la pasarela
    public ResponseEntity<String> confirmarPago(@RequestBody PasarelaCallback cb) {
        service.processCallback(cb);
        return ResponseEntity.ok("OK");
    }
    public record PagoInitResponse(String urlRedirect, String transaccionId) {}
    public record PasarelaCallback(String transaccionId, String status, String firma) {}

}
