package com.ramon.almacen.services.productos;

import com.ramon.almacen.dto.productos.ProductoRequest;
import com.ramon.almacen.dto.productos.ProductoResponse;
import com.ramon.almacen.entities.Producto;
import com.ramon.almacen.enums.Categoria;
import com.ramon.almacen.exceptions.DatoInvalidoException;
import com.ramon.almacen.exceptions.RecursoNoEncontradoException;
import com.ramon.almacen.mappers.ProductoMapper;
import com.ramon.almacen.repositories.ProductoRepository;
import com.ramon.almacen.specificacions.ProductSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre, String categoria,
                                         BigDecimal precioMin, BigDecimal precioMax) {

        Specification<Producto> spec = Specification
                .where(ProductSpecs.conNombre(nombre))
                .and(ProductSpecs.conCategoria(categoria))
                .and(ProductSpecs.conPrecioMin(precioMin))
                .and(ProductSpecs.conPrecioMax(precioMax));

        return productoRepository.findAll(spec)
                .stream()
                .map(productoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(buscar(id));
    }

    @Override
    @Transactional
    public ProductoResponse registrar(ProductoRequest request) {
        Producto producto = productoMapper.requestAEntidad(request, convertirCategoria(request.categoria()));
        return productoMapper.entidadAResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = buscar(id);
        Producto.validarDatos(request.nombre(), convertirCategoria(request.categoria()),
                request.precio(), request.cantidad());
        producto.actualizar(request.nombre(), convertirCategoria(request.categoria()),
                request.precio(), request.cantidad());
        return productoMapper.entidadAResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        productoRepository.delete(buscar(id));
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));
    }

    private Categoria convertirCategoria(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String limpio = valor.trim();

        try {
            return Categoria.valueOf(limpio.toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        try {
            return Categoria.obtenerCategoriaPorDescripcion(limpio);
        } catch (Exception e) {
            throw new DatoInvalidoException("Categoría no válida: " + valor);
        }
    }
}