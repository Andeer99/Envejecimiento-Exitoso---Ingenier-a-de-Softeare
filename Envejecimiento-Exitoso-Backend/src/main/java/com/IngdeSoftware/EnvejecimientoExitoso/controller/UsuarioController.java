package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /* ---------- Crear cliente ---------- */
    @PostMapping("/cliente")
    public ResponseEntity<Usuario> crearCliente(@Valid @RequestBody Usuario usuario) {
        Usuario creado = usuarioService.createCliente(usuario);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    /* ---------- Crear administrador ---------- */
    @PreAuthorize("hasRole('ADMIN')")          // solo un admin puede crear otros admins
    @PostMapping("/admin")
    public ResponseEntity<Usuario> crearAdmin(@Valid @RequestBody Usuario usuario) {
        Usuario creado = usuarioService.createAdmin(usuario);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    /* ---------- Listar todos ---------- */
    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    /* ---------- Obtener uno ---------- */
    @GetMapping("/{id}")
    public Usuario findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    /* ---------- Actualizar ---------- */
    @PutMapping("/{id}")
    public Usuario update(@PathVariable Long id,
                          @Valid @RequestBody Usuario cambios) {
        return usuarioService.update(id, cambios);
    }

    /* ---------- Eliminar ---------- */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")         // protege la eliminación
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
