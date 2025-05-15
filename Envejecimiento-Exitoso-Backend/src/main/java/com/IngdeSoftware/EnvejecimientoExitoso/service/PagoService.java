package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.controller.PagoController;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.pago.PagoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.PagoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pago;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pedido;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.PagoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.PedidoRepository;
import org.springframework.http.HttpEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;


@Service
@Transactional

public class PagoService {

    private final PedidoRepository pedidoRepo;
    private final PagoRepository   pagoRepo;
    private final PagoMapper       pagoMapper;
    private final RestTemplate     rest;
    private final String           pasarelaUrl;

    public PagoService(PedidoRepository pedidoRepo,
                       PagoRepository pagoRepo,
                       PagoMapper pagoMapper,
                       RestTemplate rest,
                       @Value("${pasarela.url}") String pasarelaUrl) {
        this.pedidoRepo   = pedidoRepo;
        this.pagoRepo     = pagoRepo;
        this.pagoMapper   = pagoMapper;
        this.rest         = rest;
        this.pasarelaUrl  = pasarelaUrl;
    }
    /* ---------- MÉTODOS PÚBLICOS ---------- */

    public PagoDTO getPago(Long pagoId) {
        Pago pago = pagoRepo.findById(pagoId)
                .orElseThrow(() -> new EntityNotFoundException("Pago inexistente"));
        return pagoMapper.toDto(pago);
    }

    public PagoInitResponse initPayment(Long pedidoId, String email) {
        var pedido = pedidoRepo.findByIdAndUsuarioEmail(pedidoId, email)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        // 1) creo pago estado PENDING
        Pago pago = pagoRepo.save(Pago.fromPedido(pedido));

        // 2) llamo a la pasarela
        HttpEntity<?> entity = new HttpEntity<>(buildBody(pedido, pago), buildHeaders());
        ResponseEntity<String> response = rest.exchange(
                pasarelaUrl + "/init", HttpMethod.POST, entity, String.class);

        pago.setPasarelaPayload(response.getBody());
        pagoRepo.save(pago);

        // 3) construyo respuesta
        String redirect = extractRedirectUrl(response.getBody());
        return new PagoInitResponse(redirect, pago.getId().toString());
    }

    public void processCallback(PasarelaCallback cb) {
        Pago pago = pagoRepo.findByPasarelaId(cb.transaccionId())
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado"));
        pago.actualizarEstado(cb.status());
        pagoRepo.save(pago);
    }

    /* ---------- HELPERS PRIVADOS ---------- */

    private HttpHeaders buildHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    private Map<String,Object> buildBody(Pedido pedido, Pago pago) {
        return Map.of(
                "amount",        pago.getMonto(),
                "currency",      "MXN",
                "orderId",       pedido.getId(),
                "transactionId", pago.getId(),
                "callbackUrl",   "https://tu-dominio.com/api/pagos/confirmacion"
        );
    }

    private String extractRedirectUrl(String bodyJson) {
        try {
            var node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(bodyJson);
            return node.path("redirect").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parseando redirect de pasarela", e);
        }
    }

    // DTOs anidados o importa de tu PagoController
    public record PagoInitResponse(String redirectUrl, String pagoId) {}
    public record PasarelaCallback(String transaccionId, String status) {}
}
