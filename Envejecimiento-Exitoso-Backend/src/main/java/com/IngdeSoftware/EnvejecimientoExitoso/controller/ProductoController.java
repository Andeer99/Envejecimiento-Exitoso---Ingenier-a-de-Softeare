// src/main/java/com/IngdeSoftware/EnvejecimientoExitoso/controller/ProductoController.java
package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    /** 1) Crear producto (solo ADMIN) */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO crear(@RequestBody @Valid ProductoCreateDTO dto) {
        return service.create(dto);
    }

    /** 2) Listar todos (cualquiera) */
    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return service.findAll();
    }

    /** 3) Obtener por id (cualquiera) */
    @GetMapping("/{id}")
    public ProductoResponseDTO detalle(@PathVariable Long id) {
        return service.findByIdDTO(id);
    }

    /** 4) Actualizar producto (solo ADMIN) */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProductoCreateDTO dto) {
        return service.update(id, dto);
    }

    /** 5) Eliminar producto (solo ADMIN) */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.delete(id);
    }
}
