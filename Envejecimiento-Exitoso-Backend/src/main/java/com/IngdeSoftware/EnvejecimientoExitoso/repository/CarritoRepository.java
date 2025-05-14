package com.IngdeSoftware.EnvejecimientoExitoso.repository;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @Query("select c from Carrito c where c.usuario.email = :email")
    Optional<Carrito> findByUsuarioEmail(String email);

    /** Devuelve un carrito existente o crea uno nuevo en memoria (se persiste fuera) */
    default Carrito obtenerOCrear(String email) {
        return findByUsuarioEmail(email).orElseGet(() -> {
            Carrito nuevo = new Carrito();
            nuevo.setUsuarioEmail(email);  // asume campo helper; ajusta a tu modelo real
            return save(nuevo);
        });
    }
}
