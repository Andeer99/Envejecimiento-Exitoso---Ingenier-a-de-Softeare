package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService service;

    @GetMapping
    public List<ProductoDTO> listar() {                         // catálogo completo
        return service.findAllDTO();
    }

    @GetMapping("/{id}")
    public ProductoDTO detalle(@PathVariable Long id) {
        return service.findDTOById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crear(@Valid @RequestBody ProductoDTO dto) {
        return service.save(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Long id,
                                  @Valid @RequestBody ProductoDTO dto) {
        dto.setId(id);
        return service.save(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.deleteById(id);
    }
}
