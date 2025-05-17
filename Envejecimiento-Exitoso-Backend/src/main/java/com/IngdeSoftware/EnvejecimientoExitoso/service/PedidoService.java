package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.PedidoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.EstadoPedido;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Pedido;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository repo;
    private final PedidoMapper     mapper;
    private final CarritoService   carritoService;

    public PedidoService(PedidoRepository repo,
                         PedidoMapper mapper,
                         CarritoService carritoService) {
        this.repo            = repo;
        this.mapper          = mapper;
        this.carritoService  = carritoService;
    }

    /* ---------- Crear pedido desde carrito ---------- */
    public PedidoDTO createOrder(String email) {

        Carrito carrito = carritoService.fetchEntity(email);

        if (carrito.getItems().isEmpty())
            throw new IllegalStateException("El carrito está vacío");

        Pedido pedido = Pedido.crearDesdeCarrito(carrito);
        Pedido saved  = repo.save(pedido);

        // vaciamos el carrito
        carrito.vaciar();
        carritoService.saveEntity(carrito);

        return mapper.toDto(saved);
    }

    /* ---------- Consultas ---------- */
    public List<PedidoDTO> findOrdersByUser(String email) {
        return mapper.toDto(repo.findByUsuarioEmailOrderByFechaDesc(email));
    }

    public PedidoDTO findByIdDTO(Long id) {
        Pedido p = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no existe"));
        return mapper.toDto(p);
    }
    @Transactional
    public PedidoDTO cancelarPedido(Long id, String username) {
        Pedido pedido = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no existe"));
        // opcional: checar que pedido.getUser().equals(username)

        pedido.setEstado(EstadoPedido.CANCELADO);
        repo.save(pedido);

        return mapper.toDto(pedido);
    }

}
