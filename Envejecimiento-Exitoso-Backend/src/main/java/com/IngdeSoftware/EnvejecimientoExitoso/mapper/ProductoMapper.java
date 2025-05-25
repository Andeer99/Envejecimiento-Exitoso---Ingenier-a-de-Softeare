package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel        = "spring",
        unmappedTargetPolicy  = ReportingPolicy.IGNORE
)
public interface ProductoMapper {

    // ---------- Entidad → DTO ----------
    @Mapping(source = "imageUrl", target = "imageUrl")
    ProductoResponseDTO toDto(Producto producto);

    List<ProductoResponseDTO> toDtoList(List<Producto> productos);

    // ---------- DTO creación → Entidad ----------
    @Mapping(target = "id",      ignore = true)
    @Mapping(source = "imageUrl", target = "imageUrl")
    Producto toEntity(ProductoCreateDTO dto);

    // ---------- DTO actualización → Entidad (in-place) ----------
    @Mapping(target = "id",      ignore = true)
    @Mapping(source = "imageUrl", target = "imageUrl")
    void updateEntity(ProductoCreateDTO dto, @MappingTarget Producto entity);
}
