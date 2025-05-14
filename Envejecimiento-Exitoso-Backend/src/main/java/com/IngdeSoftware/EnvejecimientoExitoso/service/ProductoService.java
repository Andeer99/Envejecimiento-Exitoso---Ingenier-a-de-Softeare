package com.IngdeSoftware.EnvejecimientoExitoso.service;
import jakarta.persistence.EntityNotFoundException;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.CarritoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.CarritoItem;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.CarritoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository repo;
    private final ProductoMapper mapper;

    public List<ProductoDTO> findAllDTO() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    public ProductoDTO findDTOById(Long id) {
        return mapper.toDto(buscar(id));
    }

    public ProductoDTO save(ProductoDTO dto) {
        Producto entity = mapper.toEntity(dto);
        return mapper.toDto(repo.save(entity));
    }

    public void deleteById(Long id) {
        repo.delete(buscar(id));
    }

    /* -- privado -- */
    private Producto buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto id=" + id + " no existe"));
    }
}