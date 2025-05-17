// src/main/java/com/IngdeSoftware/EnvejecimientoExitoso/mapper/ProductoMapper.java
package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

    // --- Entidad → DTO ---
    @Mapping(source = "id",          target = "id")
    @Mapping(source = "nombre",      target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "precio",      target = "precio")
    @Mapping(source = "stock",       target = "stock")
    @Mapping(source = "categoria",   target = "categoria")
    ProductoResponseDTO toDto(Producto producto);

    // --- DTO → Entidad (para creación) ---
    @Mapping(target = "id",           ignore = true) // no inyectamos id al crear
    @Mapping(source = "nombre",       target = "nombre")
    @Mapping(source = "descripcion",  target = "descripcion")
    @Mapping(source = "precio",       target = "precio")
    @Mapping(source = "stock",        target = "stock")
    @Mapping(source = "categoria",    target = "categoria")
    Producto toEntity(ProductoCreateDTO createDTO);

    // --- DTO → Entidad (para actualización in-place) ---
    @Mapping(target = "id",           ignore = true) // mantenemos el id original
    @Mapping(source = "nombre",       target = "nombre")
    @Mapping(source = "descripcion",  target = "descripcion")
    @Mapping(source = "precio",       target = "precio")
    @Mapping(source = "stock",        target = "stock")
    @Mapping(source = "categoria",    target = "categoria")
    void updateEntity(ProductoCreateDTO dto, @MappingTarget Producto entity);
}
