package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pago.PasarelaCallback;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.pago.PagoDTO;          // si lo necesitas en algún endpoint
import com.IngdeSoftware.EnvejecimientoExitoso.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@PreAuthorize("hasRole('CLIENTE')")
public class PagoController {

    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    /**
     * Paso 1: cliente inicia el pago para un pedido.
     *   POST /api/pagos/iniciar?pedidoId=123
     */
    @PostMapping("/iniciar")
    public PagoService.PagoInitResponse iniciarPago(
            @RequestParam Long pedidoId,
            Authentication auth
    ) {
        // auth.getName() es el email del usuario logueado
        return service.initPayment(pedidoId, auth.getName());
    }

    /**
     * Paso 2: la pasarela nos llama para confirmar el resultado.
     *   POST /api/pagos/confirmacion
     *   Body: { "transaccionId": "...", "status": "...", "firma": "..." }
     */
    @PostMapping("/confirmacion")
    public ResponseEntity<Void> confirmarPago(@RequestBody PagoService.PasarelaCallback callback) {
        service.processCallback(callback);
        return ResponseEntity.ok().build();
    }

    /**
     * (Opcional) Si quieres exponer un endpoint para consultar un pago:
     *   GET /api/pagos/{id}
     */
    @GetMapping("/{id}")
    public PagoDTO obtenerPago(@PathVariable Long id) {
        return service.getPago(id);
    }
}
