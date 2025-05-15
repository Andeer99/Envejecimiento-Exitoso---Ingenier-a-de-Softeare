package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@PreAuthorize("hasRole('CLIENTE')")          // solo clientes autenticados
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    /** Obtener el carrito del usuario actual */
    @GetMapping
    public CarritoDTO verCarrito(Authentication auth) {
        return service.getCart(auth.getName());
    }

    /** Añadir producto al carrito */
    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoDTO addItem(@Valid @RequestBody CarritoItemDTO item,
                              Authentication auth) {
        return service.addItem(auth.getName(), item);
    }

    /** Actualizar cantidad */
    @PutMapping("/items/{itemId}")
    public CarritoDTO updateItem(@PathVariable Long itemId,
                                 @RequestParam int cantidad,
                                 Authentication auth) {
        return service.updateItem(auth.getName(), itemId, cantidad);
    }

    /** Eliminar ítem */
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long itemId, Authentication auth) {
        service.removeItem(auth.getName(), itemId);
    }

    /** Vaciar carrito */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(Authentication auth) {
        service.clear(auth.getName());
    }
}
