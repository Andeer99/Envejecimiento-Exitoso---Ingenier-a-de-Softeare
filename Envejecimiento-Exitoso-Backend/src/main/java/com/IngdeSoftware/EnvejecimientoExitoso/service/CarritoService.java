package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemCreateDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.CarritoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.CarritoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarritoService {

    private final CarritoRepository  carritoRepo;
    private final UsuarioRepository  usuarioRepo;
    private final ProductoRepository productoRepo;
    private final CarritoMapper      mapper;

    public CarritoService(CarritoRepository carritoRepo,
                          UsuarioRepository usuarioRepo,
                          ProductoRepository productoRepo,
                          CarritoMapper mapper) {
        this.carritoRepo  = carritoRepo;
        this.usuarioRepo  = usuarioRepo;
        this.productoRepo = productoRepo;
        this.mapper       = mapper;
    }

    /* ---------- helpers ---------- */
    private Carrito fetchOrCreateCart(String email) {
        Usuario u = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return carritoRepo.findByUsuarioEmail(email)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    c.setUsuario(u);
                    return carritoRepo.save(c);
                });
    }

    /* ---------- API ---------- */
    public CarritoDTO getCart(String email) {
        return mapper.toDto(fetchOrCreateCart(email));
    }

    public CarritoDTO addItem(String email, CarritoItemCreateDTO dto) {
        Carrito c = fetchOrCreateCart(email);
        Producto p = productoRepo.findById(dto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
        c.agregar(p, dto.cantidad());
        return mapper.toDto(carritoRepo.save(c));
    }

    public CarritoDTO updateItem(String email, Long itemId, int cantidad) {
        Carrito c = fetchOrCreateCart(email);
        c.actualizarCantidad(itemId, cantidad);
        return mapper.toDto(carritoRepo.save(c));
    }

    public void removeItem(String email, Long itemId) {
        Carrito c = fetchOrCreateCart(email);
        c.eliminarItem(itemId);
        carritoRepo.save(c);
    }

    public void clear(String email) {
        Carrito c = fetchOrCreateCart(email);
        c.vaciar();
        carritoRepo.save(c);
    }

    /* Expuesto para PedidoService */
    public Carrito fetchEntity(String email)          { return fetchOrCreateCart(email); }
    public Carrito saveEntity(Carrito carrito)        { return carritoRepo.save(carrito); }
}
