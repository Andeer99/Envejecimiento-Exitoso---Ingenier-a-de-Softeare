package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    /* Entidad → DTO */
    ProductoDTO toDto(Producto entity);

    /* DTO → Entidad */
    Producto toEntity(ProductoDTO dto);

    /* Listado */
    List<ProductoDTO> toDto(List<Producto> entities);
}
