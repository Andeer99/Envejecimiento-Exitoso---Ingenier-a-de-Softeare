package com.IngdeSoftware.EnvejecimientoExitoso.repository;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByPasarelaId(String pasarelaId);
}
