package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.producto.ProductoResponseDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.ProductoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repo;
    private final ProductoMapper     mapper;

    public ProductoService(ProductoRepository repo, ProductoMapper mapper) {
        this.repo   = repo;
        this.mapper = mapper;
    }

    /** 1) Crear */
    public ProductoResponseDTO create(ProductoCreateDTO dto) {
        Producto entidad = mapper.toEntity(dto);
        Producto saved   = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /** 2) Listar todos */
    public List<ProductoResponseDTO> findAll() {
        return repo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /** 3) Obtener por id */
    public ProductoResponseDTO findByIdDTO(Long id) {
        Producto entidad = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
        return mapper.toDto(entidad);
    }

    /** 4) Actualizar */
    public ProductoResponseDTO update(Long id, ProductoCreateDTO dto) {
        Producto entidad = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
        // aquí invocamos al método que actualiza in-place tu entidad
        mapper.updateEntity(dto, entidad);
        Producto saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /** 5) Eliminar */
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Producto no existe");
        }
        repo.deleteById(id);
    }
}

