package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.cliente.ClienteUpdateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.ClienteMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Cliente;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

@Transactional
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo, ClienteMapper mapper, PasswordEncoder encoder) {
        this.repo = repo;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    private final ClienteMapper   mapper;
    private final PasswordEncoder encoder;

    public ClienteResponseDTO register(ClienteCreateDTO dto) {
        Cliente c = mapper.toEntity(dto);
        c.setPassword( encoder.encode(dto.password()) );
        c.addRole("CLIENTE");
        return mapper.toResponse( repo.save(c) );
    }

    public ClienteResponseDTO update(Long id, ClienteUpdateDTO dto) {
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente id="+id+" no existe"));
        // sólo modificamos nombre+password
        mapper.updateEntityFromDto(dto, c);
        c.setPassword( encoder.encode(dto.password()) );
        return mapper.toResponse( repo.save(c) );
    }

    public List<ClienteResponseDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ClienteResponseDTO findById(Long id) {
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente id="+id+" no existe"));
        return mapper.toResponse(c);
    }

    public void deleteById(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Cliente id="+id+" no existe");
        repo.deleteById(id);
    }
}

