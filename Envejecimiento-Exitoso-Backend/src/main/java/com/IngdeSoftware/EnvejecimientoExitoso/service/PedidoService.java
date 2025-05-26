package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.pedido.PedidoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.PedidoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.*;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.PedidoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository   repo;
    private final PedidoMapper       mapper;
    private final CarritoService     carritoService;
    private final UsuarioRepository  userRepo;        // ← nuevo
    private final ProductoRepository productoRepo;     // ← nuevo

    public PedidoService(PedidoRepository repo,
                         PedidoMapper mapper,
                         CarritoService carritoService,
                         UsuarioRepository userRepo,           // ← nuevo
                         ProductoRepository productoRepo) {    // ← nuevo
        this.repo            = repo;
        this.mapper          = mapper;
        this.carritoService  = carritoService;
        this.userRepo        = userRepo;
        this.productoRepo     = productoRepo;
    }

    /* ---------- Crear pedido desde carrito ---------- */
    @Transactional
    public PedidoDTO createOrderFromDto(String email, PedidoCreateDTO dto) {
        if (dto.items().isEmpty())
            throw new IllegalStateException("El carrito está vacío");

        // 1) Crea la entidad Pedido a mano (no usas Carrito)
        Usuario user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no existe"));
        Pedido pedido = new Pedido();
        pedido.setUsuario(user);
        for (var it : dto.items()) {
            Producto prod = productoRepo.findById(it.productoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
            pedido.getItems().add(new PedidoItem(
                    pedido,
                    prod,
                    it.cantidad(),
                    prod.getPrecio()            // o el precio que manejes
            ));
        }
        pedido.calcularTotal();

        // 2) Guarda el pedido y devuelve el DTO
        Pedido saved = repo.save(pedido);
        return mapper.toDto(saved);
    }


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
