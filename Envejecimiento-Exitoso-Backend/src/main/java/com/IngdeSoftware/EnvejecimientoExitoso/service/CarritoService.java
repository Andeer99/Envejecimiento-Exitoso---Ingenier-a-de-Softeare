package com.IngdeSoftware.EnvejecimientoExitoso.service;

import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.dto.carrito.CarritoItemDTO;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.CarritoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.ProductoRepository;
import com.IngdeSoftware.EnvejecimientoExitoso.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioRepository usuarioRepo;
    private final CarritoMapper mapper;

    public CarritoDTO getCart(String email) {
        return mapper.toDto(
                carritoRepo.obtenerOCrear(email)
        );
    }

    public CarritoDTO addItem(String email, CarritoItemDTO itemDto) {
        var carrito = carritoRepo.obtenerOCrear(email);
        var producto = productoRepo.findById(itemDto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));

        carrito.agregar(producto, itemDto.cantidad());
        return mapper.toDto(carritoRepo.save(carrito));
    }

    public CarritoDTO updateItem(String email, Long itemId, int cantidad) {
        var carrito = carritoRepo.obtenerOCrear(email);
        carrito.actualizarCantidad(itemId, cantidad);
        return mapper.toDto(carritoRepo.save(carrito));
    }

    public void removeItem(String email, Long itemId) {
        var carrito = carritoRepo.obtenerOCrear(email);
        carrito.eliminarItem(itemId);
        carritoRepo.save(carrito);
    }

    public void clear(String email) {
        var carrito = carritoRepo.obtenerOCrear(email);
        carrito.vaciar();
        carritoRepo.save(carrito);
    }
}