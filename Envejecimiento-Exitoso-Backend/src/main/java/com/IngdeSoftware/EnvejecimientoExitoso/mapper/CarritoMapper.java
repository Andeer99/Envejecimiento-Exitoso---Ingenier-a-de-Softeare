package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.*;
import com.IngdeSoftware.EnvejecimientoExitoso.model.*;

import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoMapper {

    CarritoDTO toDto(Carrito entity);
    CarritoItemDTO toDto(CarritoItem entity);

    // listas
    List<CarritoDTO> toDto(List<Carrito> list);
}
