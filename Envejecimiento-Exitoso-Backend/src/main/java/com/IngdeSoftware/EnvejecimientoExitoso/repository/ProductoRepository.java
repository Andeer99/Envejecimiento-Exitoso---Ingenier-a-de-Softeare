package com.IngdeSoftware.EnvejecimientoExitoso.repository;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Si en el catálogo filtrarás por categoría o nombre, añade métodos aquí
}
