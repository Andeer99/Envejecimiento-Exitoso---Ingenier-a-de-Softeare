package com.IngdeSoftware.EnvejecimientoExitoso.repository;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioEmail(String email);

    Optional<Pedido> findByIdAndUsuarioEmail(Long id, String email);
}
