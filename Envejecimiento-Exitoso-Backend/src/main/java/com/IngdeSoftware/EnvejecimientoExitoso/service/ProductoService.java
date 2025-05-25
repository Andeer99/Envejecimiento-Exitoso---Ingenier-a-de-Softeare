package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.ProductoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repo;
    private final ProductoMapper mapper;

    public List<ProductoResponseDTO> findAll() {
        return repo.findAllProjected();
    }

    public ProductoResponseDTO findByIdDTO(Long id) {
        return repo.findAllProjected()
                   .stream()
                   .filter(d -> d.id().equals(id))
                   .findFirst()
                   .orElseThrow(() ->
                       new EntityNotFoundException("Producto id="+id+" no encontrado"));
    }

    public ProductoResponseDTO create(ProductoCreateDTO dto) {
        Producto saved = repo.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public ProductoResponseDTO update(Long id, ProductoCreateDTO dto) {
        Producto entity = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto id="+id+" no existe"));
        mapper.updateEntity(dto, entity);
        entity.setImageUrl(dto.imageUrl());
        return mapper.toDto(repo.save(entity));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Producto id="+id+" no existe");
        }
        repo.deleteById(id);
    }
}
