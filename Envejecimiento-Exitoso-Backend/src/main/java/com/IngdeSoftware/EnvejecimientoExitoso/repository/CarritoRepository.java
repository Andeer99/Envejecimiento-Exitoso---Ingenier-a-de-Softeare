package com.IngdeSoftware.EnvejecimientoExitoso.repository;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioEmail(String email);
}
