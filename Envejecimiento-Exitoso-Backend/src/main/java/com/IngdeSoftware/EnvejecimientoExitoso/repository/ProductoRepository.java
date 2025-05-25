package com.IngdeSoftware.EnvejecimientoExitoso.repository;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /* ---------- PROYECCIONES READ-ONLY ---------- */

    /**
     * Devuelve todos los productos mapeados directamente a ProductoResponseDTO
     * (incluye imageUrl, por eso no se pierde en el JSON).
     */
    @Query("""
           SELECT new com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO(
                    p.id, p.nombre, p.descripcion, p.precio, p.stock,
                    p.imageUrl, p.categoria)
           FROM   Producto p
           """)
    List<ProductoResponseDTO> findAllProjected();

    /* ---------- BÚSQUEDAS ESPECÍFICAS ---------- */

    List<ProductoResponseDTO> findByCategoriaIgnoreCase(String categoria);

    List<ProductoResponseDTO> findByNombreContainingIgnoreCase(String nombre);
}
