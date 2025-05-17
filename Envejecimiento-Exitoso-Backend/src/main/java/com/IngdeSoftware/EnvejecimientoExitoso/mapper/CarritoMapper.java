package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.CarritoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoMapper {

    /* ---- entidad → DTO de carrito ---- */
    @Mappings({
            @Mapping(target = "items", source = "items"),
            @Mapping(target = "total", expression = "java(carrito.getTotal())")
    })
    CarritoDTO toDto(Carrito carrito);

    List<CarritoDTO> toDto(List<Carrito> carritos);

    /* ---- ítem de carrito ---- */
    @Mappings({
            @Mapping(target = "productoId",
                    expression = "java(ci.getProducto()!=null ? ci.getProducto().getId() : null)"),
            @Mapping(target = "nombreProducto",
                    expression = "java(ci.getProducto()!=null ? ci.getProducto().getNombre() : null)"),
            @Mapping(target = "subtotal",
                    expression = "java(ci.getSubtotal())")
    })
    CarritoItemDTO toDto(CarritoItem ci);

    List<CarritoItemDTO> toDtoItems(List<CarritoItem> items);
}
