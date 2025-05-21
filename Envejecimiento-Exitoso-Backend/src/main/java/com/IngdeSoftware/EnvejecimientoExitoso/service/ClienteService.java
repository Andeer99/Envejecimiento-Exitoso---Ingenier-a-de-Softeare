package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteUpdateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.ClienteMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Cliente;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Role;
import com.IngdeSoftware.EnvejecimientoExitoso.model.RoleName;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ClienteRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repo;
    private final RoleRepository    roleRepo;
    private final ClienteMapper     mapper;
    private final PasswordEncoder   encoder;

    /* ---------- REGISTRO ---------- */
    public ClienteResponseDTO register(ClienteCreateDTO dto) {
        Cliente c = mapper.toEntity(dto);
        c.setPassword( encoder.encode(dto.password()) );

        Role clienteRole = roleRepo.findByName(RoleName.CLIENTE)
                .orElseThrow(() ->
                        new IllegalStateException("Falta seed CLIENTE"));
        c.addRole(clienteRole);

        return mapper.toResponse( repo.save(c) );
    }

    /* ---------- LISTAR TODOS ---------- */
    public List<ClienteResponseDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- OBTENER UNO ---------- */
    public ClienteResponseDTO findById(Long id) {
        Cliente c = repo.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cliente id = " + id + " no existe"));
        return mapper.toResponse(c);
    }

    /* ---------- ACTUALIZAR ---------- */
    public ClienteResponseDTO update(Long id, ClienteUpdateDTO dto) {
        Cliente c = repo.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cliente id = " + id + " no existe"));

        mapper.updateEntityFromDto(dto, c);     // ← usa tu método MapStruct o manual
        return mapper.toResponse( repo.save(c) );
    }

    /* ---------- ELIMINAR ---------- */
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Cliente id = " + id + " no existe");
        }
        repo.deleteById(id);
    }
}
