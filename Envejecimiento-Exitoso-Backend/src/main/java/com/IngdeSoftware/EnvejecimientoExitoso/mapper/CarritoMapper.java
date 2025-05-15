package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.CarritoItem;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoMapper {

    CarritoDTO toDto(Carrito carrito);

    CarritoItemDTO toItemDto(CarritoItem item);

    // Si más adelante necesitas mapear listas:
    List<CarritoDTO> toDtoList(List<Carrito> carritos);
}
