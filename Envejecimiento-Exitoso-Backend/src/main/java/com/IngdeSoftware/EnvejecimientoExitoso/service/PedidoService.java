package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.PedidoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pedido;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service

@Transactional
public class PedidoService {
    public PedidoService(CarritoService carritoService, PedidoRepository pedidoRepo, PedidoMapper mapper) {
        this.carritoService = carritoService;
        this.pedidoRepo = pedidoRepo;
        this.mapper = mapper;
    }

    private final CarritoService     carritoService;
    private final PedidoRepository   pedidoRepo;
    private final PedidoMapper       mapper;

    /** Alias de createFromCart para que el controlador lo encuentre */
    public PedidoDTO createOrder(String email) {
        // 1) Obtiene el carrito
        Carrito carrito = carritoService.fetchEntity(email);
        // 2) Fabrica el pedido
        Pedido pedido = Pedido.crearDesdeCarrito(carrito);
        // 3) Vacía y guarda el carrito
        carrito.vaciar();
        carritoService.saveEntity(carrito);
        // 4) Persiste el pedido
        Pedido saved = pedidoRepo.save(pedido);
        // 5) Mapea a DTO
        return mapper.toDto(saved);
    }

    /** Devuelve todos los pedidos del usuario */
    public List<PedidoDTO> findOrdersByUser(String email) {
        // Asume que tienes un método en el repo como findByUsuarioEmail(...)
        List<Pedido> lista = pedidoRepo.findByUsuarioEmail(email);
        return lista.stream()
                .map(mapper::toDto)
                .toList();
    }

    /** Devuelve un pedido por id o lanza 404 */
    public PedidoDTO findByIdDTO(Long id) {
        Pedido p = pedidoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido id=" + id + " no existe"));
        return mapper.toDto(p);
    }

    // ... otros métodos (cancelar, actualizar estado, etc.)
}
