package com.ramon.almacen.mappers;

import com.ramon.almacen.dto.ventas.DetalleVentaResponse;
import com.ramon.almacen.dto.ventas.VentaResponse;
import com.ramon.almacen.entities.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VentaMapper {

    public VentaResponse entidadAResponse(Venta venta) {
        if (venta == null) {
            return null;
        }

        var detallesDto = venta.getDetalleVentas().stream().map(detalle -> {
            BigDecimal subtotal = detalle.getPrecioProducto()
                    .multiply(BigDecimal.valueOf(detalle.getCantidadProducto()));

            return new DetalleVentaResponse(
                    detalle.getProducto().getId(),
                    detalle.getProducto().getNombre(),
                    detalle.getCantidadProducto(),
                    detalle.getPrecioProducto(),
                    subtotal
            );
        }).toList();

        return new VentaResponse(
                venta.getId(),
                venta.getFecha() != null ? venta.getFecha().toString() : null,
                venta.getEstadoVenta() != null ? venta.getEstadoVenta().getDescripcion() : null,
                null, // Nota: si tienes el mapper de sucursal inyectado aquí, puedes mapear la sucursal en lugar de null si lo requieres.
                detallesDto,
                venta.getTotal()
        );
    }
}