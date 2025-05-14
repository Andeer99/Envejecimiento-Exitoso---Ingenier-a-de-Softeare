package com.IngdeSoftware.EnvejecimientoExitoso.service;


import com.IngdeSoftware.EnvejecimientoExitoso.model.Cliente;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository repo;
    private final PasswordEncoder encoder;     // Reutilizamos el encoder de UsuarioService

    public ClienteService(ClienteRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /* -------- CRUD básicos -------- */

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Cliente findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cliente id=" + id + " no encontrado"));
    }

    /** Registro/actualización (hash de password heredado de Usuario) */
    public Cliente save(Cliente cliente) {
        if (cliente.getPassword() != null && !cliente.getPassword().startsWith("$2")) { // BCrypt?
            cliente.setPassword(encoder.encode(cliente.getPassword()));
        }
        // regla de negocio: rol CLIENTE  (si manejas roles)
        //cliente.addRole("CLIENTE");
        return repo.save(cliente);
    }

    public void deleteById(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Cliente id=" + id + " no existe");
        repo.deleteById(id);
    }

    /* -------- reglas adicionales -------- */

    public boolean existsByRfc(String rfc) {
        return repo.findByRfc(rfc).isPresent();
    }
}
