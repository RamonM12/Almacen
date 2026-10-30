package com.ramon.almacen.services.ventas;

import com.ramon.almacen.dto.ventas.VentaRequest;
import com.ramon.almacen.dto.ventas.VentaResponse;
import com.ramon.almacen.entities.DetalleVenta;
import com.ramon.almacen.entities.Producto;
import com.ramon.almacen.entities.Sucursal;
import com.ramon.almacen.entities.Venta;
import com.ramon.almacen.enums.EstadoVenta;
import com.ramon.almacen.exceptions.RecursoNoEncontradoException;
import com.ramon.almacen.mappers.VentaMapper;
import com.ramon.almacen.repositories.ProductoRepository;
import com.ramon.almacen.repositories.SucursalRepository;
import com.ramon.almacen.repositories.VentasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentasRepository ventasRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        return ventasRepository.findByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true) // Corregido el typo si lo tenías
    public VentaResponse obtenerPorIdActiva(Long id) {
        Venta venta = ventasRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta activa no encontrada con ID: " + id));
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + request.idSucursal()));

        Venta venta = Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();

        for (var itemRequest : request.productos()) {
            Long productoId = itemRequest.idProducto();
            Integer cantidadSolicitada = itemRequest.cantidadProducto();

            Producto producto = productoRepository.findByIdForUpdate(productoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));


            producto.descontarCantidad(cantidadSolicitada);
            productoRepository.save(producto);

            // Fotografía exacta del precio actual de la BD
            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .cantidadProducto(cantidadSolicitada)
                    .precioProducto(producto.getPrecio())
                    .build();

            venta.agregarDetalle(detalle);
        }

        Venta ventaGuardada = ventasRepository.save(venta);
        return ventaMapper.entidadAResponse(ventaGuardada);
    }

    @Override
    @Transactional
    public void cancelar(Long id) {
        Venta venta = ventasRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));

        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
            productoRepository.save(producto);
        }

        ventasRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        return ventasRepository.findByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }
}