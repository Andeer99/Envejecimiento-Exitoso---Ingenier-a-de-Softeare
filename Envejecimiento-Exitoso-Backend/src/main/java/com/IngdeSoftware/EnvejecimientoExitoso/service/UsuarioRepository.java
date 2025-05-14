package com.IngdeSoftware.EnvejecimientoExitoso.service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
