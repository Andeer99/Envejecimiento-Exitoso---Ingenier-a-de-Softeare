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

    /**
     * CREATE: de ClienteCreateDTO → Cliente
     * Ignoramos id y roles (serán generados/asignados en el servicio).
     */
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "roles",    ignore = true)
    @Mapping(target = "nombre",   source = "nombreCompleto")
    @Mapping(target = "email",    source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "rfc",      source = "rfc")
    Cliente toEntity(ClienteCreateDTO dto);

    /**
     * UPDATE: de ClienteUpdateDTO → Cliente existente
     * Sólo cambiamos nombre y password; email, rfc, roles e id se dejan intactos.
     */
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "email",    ignore = true)
    @Mapping(target = "rfc",      ignore = true)
    @Mapping(target = "roles",    ignore = true)
    @Mapping(target = "nombre",   source = "nombreCompleto")
    @Mapping(target = "password", source = "password")
    void updateEntityFromDto(ClienteUpdateDTO dto, @MappingTarget Cliente entity);

    /**
     * READ: de Cliente → ClienteResponseDTO
     * Mapeo explícito de cada campo de salida.
     */
    @Mapping(target = "id",             source = "id")
    @Mapping(target = "nombreCompleto", source = "nombre")
    @Mapping(target = "email",          source = "email")
    @Mapping(target = "rfc",            source = "rfc")
    ClienteResponseDTO toResponse(Cliente entity);

    /** (Opcional) conveniencia para listas */
    List<ClienteResponseDTO> toResponseList(List<Cliente> entities);
}
