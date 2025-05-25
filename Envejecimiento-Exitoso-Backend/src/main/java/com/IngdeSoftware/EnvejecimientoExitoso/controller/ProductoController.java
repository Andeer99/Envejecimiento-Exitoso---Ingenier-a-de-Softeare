package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService servicio;

    /* ---------- CRUD ---------- */

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return servicio.findAll();
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO detalle(@PathVariable Long id) {
        return servicio.findByIdDTO(id);
    }

    @PostMapping
    public ProductoResponseDTO crear(@RequestBody ProductoCreateDTO dto) {
        return servicio.create(dto);
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Long id,
                                          @RequestBody ProductoCreateDTO dto) {
        return servicio.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicio.delete(id);
    }

    /* ---------- endpoint de debug opcional ---------- */
    // @GetMapping("/debug/raw")
    // public List<Producto> raw(ProductoRepository repo) { return repo.findAll(); }
}
