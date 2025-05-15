package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface ProductoMapper {
    // 1) CREATE: ignoro id (autogenerado)
    @Mapping(target="id",        ignore = true)
    @Mapping(target="nombre",    source = "nombre")
    @Mapping(target="descripcion",source = "descripcion")
    @Mapping(target="precio",    source = "precio")
    @Mapping(target="stock",     source = "stock")
    @Mapping(target="categoria", source = "categoria")
    Producto toEntity(ProductoCreateDTO dto);

    // 2) READ: entity → response
    @Mapping(target="id",         source = "id")
    @Mapping(target="nombre",     source = "nombre")
    @Mapping(target="descripcion",source = "descripcion")
    @Mapping(target="precio",     source = "precio")
    @Mapping(target="stock",      source = "stock")
    @Mapping(target="categoria",  source = "categoria")
    ProductoResponseDTO toDto(Producto entity);
}
