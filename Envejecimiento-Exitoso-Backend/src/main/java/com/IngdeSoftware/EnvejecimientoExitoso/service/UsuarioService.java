package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.service.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo,
                          PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /**
     * Devuelve todos los usuarios.
     */
    public List<Usuario> findAll() {
        return repo.findAll();
    }

    /**
     * Busca un usuario por su ID o lanza excepción si no existe.
     */
    public Usuario findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuario no encontrado: " + id)
                );
    }

    /**
     * Busca un usuario por su email o lanza excepción si no existe.
     */
    public Usuario findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuario no encontrado: " + email)
                );
    }

    /**
     * Crea o actualiza un usuario.
     * - En creación: siempre encripta la contraseña.
     * - En edición: si no llega nueva contraseña, conserva la anterior.
     */
    public Usuario save(Usuario usuario) {
        if (usuario.getId() != null) {
            // Edición: obtenemos el existente para conservar contraseña si no cambia
            Usuario existente = findById(usuario.getId());
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                usuario.setPassword(existente.getPassword());
            } else {
                usuario.setPassword(encoder.encode(usuario.getPassword()));
            }
        } else {
            // Creación: encriptamos siempre
            usuario.setPassword(encoder.encode(usuario.getPassword()));
        }
        return repo.save(usuario);
    }

    /**
     * Elimina un usuario por ID.
     */
    public void deleteById(Long id) {
        // opcional: verificar existencia antes de borrar
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("No existe usuario con ID: " + id);
        }
        repo.deleteById(id);
    }
}
