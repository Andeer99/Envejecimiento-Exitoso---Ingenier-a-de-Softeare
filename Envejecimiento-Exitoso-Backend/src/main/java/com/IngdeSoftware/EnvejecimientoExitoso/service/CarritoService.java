package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.mapper.CarritoMapper;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Carrito;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Producto;
import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.CarritoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class CarritoService {

    private final CarritoRepository carritoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;
    private final CarritoMapper mapper;

    /** Obtener o crear carrito */
    private Carrito fetchOrCreateCart(String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return carritoRepo.findByUsuarioEmail(email)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    return carritoRepo.save(nuevo);
                });
    }

    /** Ver carrito */
    public CarritoDTO getCart(String email) {
        Carrito carrito = fetchOrCreateCart(email);
        return mapper.toDto(carrito);
    }

    /** Añadir ítem */
    public CarritoDTO addItem(String email, CarritoItemDTO itemDto) {
        Carrito carrito = fetchOrCreateCart(email);
        Producto producto = productoRepo.findById(itemDto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
        carrito.agregar(producto, itemDto.cantidad());
        return mapper.toDto(carritoRepo.save(carrito));
    }

    /** Actualizar cantidad */
    public CarritoDTO updateItem(String email, Long itemId, int cantidad) {
        Carrito carrito = fetchOrCreateCart(email);
        carrito.actualizarCantidad(itemId, cantidad);
        return mapper.toDto(carritoRepo.save(carrito));
    }

    /** Eliminar ítem */
    public void removeItem(String email, Long itemId) {
        Carrito carrito = fetchOrCreateCart(email);
        carrito.eliminarItem(itemId);
        carritoRepo.save(carrito);
    }

    /** Vaciar carrito */
    public void clear(String email) {
        Carrito carrito = fetchOrCreateCart(email);
        carrito.vaciar();
        carritoRepo.save(carrito);
    }
}
