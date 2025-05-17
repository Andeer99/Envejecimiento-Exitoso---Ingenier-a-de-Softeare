package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pedido;
import com.IngdeSoftware.EnvejecimientoExitoso.model.PedidoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    /* ---- Pedido completo ---- */
    @Mappings({
            @Mapping(target = "items", source = "items"),
            @Mapping(target = "total", source = "total")
    })
    PedidoDTO toDto(Pedido pedido);

    List<PedidoDTO> toDto(List<Pedido> pedidos);

    /* ---- Ítem del pedido ---- */
    @Mappings({
            @Mapping(target = "productoId",
                    expression = "java(pi.getProducto()!=null ? pi.getProducto().getId() : null)"),
            @Mapping(target = "nombreProducto",
                    expression = "java(pi.getProducto()!=null ? pi.getProducto().getNombre() : null)"),
            @Mapping(target = "subtotal",
                    expression = "java(pi.getSubtotal())")
    })
    PedidoItemDTO toDto(PedidoItem pi);

    List<PedidoItemDTO> toDtoItems(List<PedidoItem> items);
}
