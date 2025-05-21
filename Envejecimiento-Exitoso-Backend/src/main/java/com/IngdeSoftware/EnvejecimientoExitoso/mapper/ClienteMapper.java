package com.IngdeSoftware.EnvejecimientoExitoso.mapper;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteUpdateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClienteMapper {

    /* ---------- CREATE ---------- */
    @Mapping(target = "nombre", source = "nombreCompleto")
    Cliente toEntity(ClienteCreateDTO dto);

    /* ---------- UPDATE ---------- */
    @Mapping(target = "nombre", source = "nombreCompleto")
    void updateEntityFromDto(ClienteUpdateDTO dto, @MappingTarget Cliente entity);

    /* ---------- READ ---------- */
    @Mapping(target = "nombreCompleto", source = "nombre")
    ClienteResponseDTO toResponse(Cliente entity);

    /* ---------- LISTA ---------- */
    List<ClienteResponseDTO> toResponseList(List<Cliente> entities);
}
