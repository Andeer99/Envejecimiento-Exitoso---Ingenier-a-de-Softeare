package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@PreAuthorize("hasRole('CLIENTE')")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    /* ---------- Ver carrito ---------- */
    @GetMapping
    public CarritoDTO verCarrito(Authentication auth) {
        return service.getCart(auth.getName());
    }

    /* ---------- Añadir ítem ---------- */
    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoDTO addItem(@Valid @RequestBody CarritoItemCreateDTO item,
                              Authentication auth) {
        return service.addItem(auth.getName(), item);
    }

    /* ---------- Actualizar cantidad ---------- */
    @PutMapping("/items/{itemId}")
    public CarritoDTO updateItem(@PathVariable Long itemId,
                                 @RequestParam int cantidad,
                                 Authentication auth) {
        return service.updateItem(auth.getName(), itemId, cantidad);
    }

    /* ---------- Eliminar ítem ---------- */
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long itemId, Authentication auth) {
        service.removeItem(auth.getName(), itemId);
    }

    /* ---------- Vaciar carrito ---------- */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(Authentication auth) {
        service.clear(auth.getName());
    }
}
