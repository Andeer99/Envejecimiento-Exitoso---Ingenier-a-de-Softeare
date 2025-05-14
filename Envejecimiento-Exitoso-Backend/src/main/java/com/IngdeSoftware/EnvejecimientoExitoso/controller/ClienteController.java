package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Cliente;
import com.IngdeSoftware.EnvejecimientoExitoso.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de clientes.
 * Endpoints bajo /api/clientes
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")            //  👉 si el front corre en otro puerto
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    /** Listado completo o paginado (según necesites) */
    @GetMapping
    public List<Cliente> listar() {
        return service.findAll();
    }

    /** Obtener un cliente por ID */
    @GetMapping("/{id}")
    public Cliente detalle(@PathVariable Long id) {
        return service.findById(id);
    }

    /** Crear un nuevo cliente (registro) */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@Valid @RequestBody Cliente cliente) {
        return service.save(cliente);
    }

    /** Actualizar (PUT = reemplazo completo) */
    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id,
                              @Valid @RequestBody Cliente cliente) {
        cliente.setId(id);
        return service.save(cliente);
    }

    /** Baja lógica/física */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
