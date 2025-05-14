package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.controller.PagoController;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pago;
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

@Service @RequiredArgsConstructor
@Transactional
public class PagoService {

    private final PedidoRepository pedidoRepo;
    private final PagoRepository pagoRepo;
    private final RestTemplate rest;                  // Bean común para HTTP
    @Value("${pasarela.url}") private String pasarelaUrl;

    /** Paso 1: cliente inicia el pago  */
    public PagoController.PagoInitResponse initPayment(Long pedidoId, String email) {
        var pedido = pedidoRepo.findByIdAndUsuarioEmail(pedidoId, email)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        // 1. Creamos entidad Pago en estado PENDING
        Pago pago = pagoRepo.save(Pago.fromPedido(pedido));

        // 2. Llamamos a la pasarela
        HttpEntity<?> entity = new HttpEntity<>(buildBody(pedido, pago), buildHeaders());
        ResponseEntity<String> response = rest.exchange(
                pasarelaUrl + "/init", HttpMethod.POST, entity, String.class);

        pago.setPasarelaPayload(response.getBody());
        pagoRepo.save(pago);

        // 3. Respuesta al front: URL a la que debe redirigirse
        String redirect = extractRedirectUrl(response.getBody());
        return new PagoController.PagoInitResponse(redirect, pago.getId().toString());
    }

    /** Paso 2: confirmación desde la pasarela */
    public void processCallback(PagoController.PasarelaCallback cb) {
        Pago pago = pagoRepo.findByPasarelaId(cb.transaccionId())
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado"));

        pago.actualizarEstado(cb.status());
        pagoRepo.save(pago);
    }

    /* -- helpers privados -- */
    private HttpHeaders buildHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }
    private Object buildBody(/*…*/) { /* contruye JSON para la pasarela */ return null; }
    private String extractRedirectUrl(String body) { /* parsea JSON */ return "https://…"; }
}