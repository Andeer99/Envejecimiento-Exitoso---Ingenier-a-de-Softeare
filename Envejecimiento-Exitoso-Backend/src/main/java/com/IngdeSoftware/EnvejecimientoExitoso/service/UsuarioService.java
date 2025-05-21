package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Role;
import com.IngdeSoftware.EnvejecimientoExitoso.model.RoleName;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.RoleRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repo;
    private final RoleRepository    roleRepo;
    private final PasswordEncoder   encoder;

    /* ---------- CRUD genérico ---------- */

    public List<Usuario> findAll() {
        return repo.findAll();
    }

    public Usuario findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario id = " + id + " no existe"));
    }

    public Usuario findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario email = " + email + " no existe"));
    }

    public void deleteById(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Usuario id = " + id + " no existe");
        repo.deleteById(id);
    }

    /* ---------- Alta con rol predeterminado ---------- */

    /**
     * Crea un nuevo cliente.
     */
    public Usuario createCliente(Usuario usuario) {
        return createWithRole(usuario, RoleName.CLIENTE);
    }

    /**
     * Crea un nuevo administrador.
     *  – Puedes añadir más lógica (p.ej. verificación de cuenta) si lo necesitas.
     */
    public Usuario createAdmin(Usuario usuario) {
        return createWithRole(usuario, RoleName.ADMIN);
    }

    /* ---------- Actualización ---------- */

    /**
     * Actualiza un usuario existente.
     *  – Si llega nueva contraseña, la encripta; si no, conserva la anterior.
     *  – Los roles no se tocan aquí (gestión aparte).
     */
    public Usuario update(Long id, Usuario cambios) {
        Usuario existente = findById(id);

        existente.setNombre( cambios.getNombre() );
        existente.setEmail(  cambios.getEmail() );

        if (cambios.getPassword() != null && !cambios.getPassword().isBlank()) {
            existente.setPassword( encoder.encode(cambios.getPassword()) );
        }

        return repo.save(existente);
    }

    /* ---------- Helpers ---------- */

    private Usuario createWithRole(Usuario usuario, RoleName roleName) {
        usuario.setPassword( encoder.encode(usuario.getPassword()) );

        Role rol = roleRepo.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException(
                        "Rol " + roleName + " no existe. ¿Seed ejecutado?"));

        usuario.addRole(rol);
        return repo.save(usuario);
    }
}
