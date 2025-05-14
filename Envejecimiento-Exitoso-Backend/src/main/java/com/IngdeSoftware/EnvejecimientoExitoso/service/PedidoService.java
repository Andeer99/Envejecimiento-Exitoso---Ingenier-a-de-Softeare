package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {

    private final PedidoRepository repo;
    private final CarritoRepository carritoRepo;
    private final UsuarioRepository usuarioRepo;
    private final PedidoMapper mapper;

    /** Crea pedido a partir del carrito, limpia el carrito y calcula totales */
    public PedidoDTO createOrder(String email) {
        var carrito = carritoRepo.obtenerOCrear(email);
        if (carrito.isEmpty()) throw new IllegalStateException("Carrito vacío");

        Pedido pedido = Pedido.crearDesdeCarrito(carrito);
        carrito.vaciar();
        carritoRepo.save(carrito);

        return mapper.toDto(repo.save(pedido));
    }

    public List<PedidoDTO> findOrdersByUser(String email) {
        return repo.findByUsuarioEmail(email).stream().map(mapper::toDto).toList();
    }

    public PedidoDTO findByIdDTO(Long id) {
        return mapper.toDto(buscar(id));
    }

    /* -- privado -- */
    private Pedido buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido id=" + id + " no existe"));
    }
}