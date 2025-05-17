package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.PedidoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pedido;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final CarritoService carritoService;    // ➜ inyecta tu CarritoService
    private final PedidoMapper mapper;

    public PedidoService(PedidoRepository pedidoRepo, PedidoMapper mapper, CarritoService carritoService) {
        this.pedidoRepo = pedidoRepo;
        this.mapper = mapper;
        this.carritoService = carritoService;
    }

    /** Crea pedido a partir del carrito, limpia el carrito y calcula totales */
    public PedidoDTO createOrder(String email) {
        // 1) Obtiene la entidad Carrito (no el DTO)
        Carrito carrito = carritoService.fetchOrCreateCartEntity(email);

        // 2) Valida que no esté vacío
        if (carrito.isEmpty()) {
            throw new IllegalStateException("Carrito vacío");
        }

        // 3) Construye el Pedido desde el Carrito
        Pedido pedido = Pedido.crearDesdeCarrito(carrito);

        // 4) Vacía el carrito (usa el método de la entidad)
        carrito.vaciar();
        carritoService.saveCarrito(carrito);         // o carritoService.clear(email)

        // 5) Persiste el pedido y retorna DTO
        return mapper.toDto(pedidoRepo.save(pedido));
    }

    public List<PedidoDTO> findOrdersByUser(String email) {
        return pedidoRepo.findByUsuarioEmail(email).stream()
                .map(mapper::toDto)
                .toList();
    }

    public PedidoDTO findByIdDTO(Long id) {
        return pedidoRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no existe"));
    }
}
