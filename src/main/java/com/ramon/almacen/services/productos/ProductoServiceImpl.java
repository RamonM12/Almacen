package com.ramon.almacen.services.productos;

import com.ramon.almacen.dto.productos.ProductoRequest;
import com.ramon.almacen.dto.productos.ProductoResponse;
import com.ramon.almacen.entities.Producto;
import com.ramon.almacen.enums.Categoria;
import com.ramon.almacen.exceptions.RecursoNoEncontradoException;
import com.ramon.almacen.mappers.ProductoMapper;
import com.ramon.almacen.repositories.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax) {
        log.info("Listando todos los productos");

        return productoRepository.findAll().stream()
               // .map(Producto producto -> productoMapper.entidadAResponse(producto)).toList();
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrar nuevo producto...");
        Producto producto = productoMapper.requestAEntidad(
                request,
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim())
        );
        productoRepository.saveAndFlush(producto);

        log.info("Nuevo producto {} registrado",producto.getNombre());

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);

        log.info("Actualizando producto con id: {}", id);

        producto.actualizar(
                request.nombre(),
                null,
                request.precio(),
                request.cantidad()
        );
        productoRepository.saveAndFlush(producto);

        log.info("Producto con id {} actualizado", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
    Producto producto = obtenerProductoOException(id);
    log.info("Eliminando producto con id: {}", id);

    productoRepository.flush();

    log.info("Producto con id {} eliminaod", id);
    }

    private Producto obtenerProductoOException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id)
        );
    }
}
