package com.ramon.almacen.repositories;

import com.ramon.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.ramon.almacen.entities.Venta;
import com.ramon.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentasRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);

    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);

    @Query("""
        SELECT new com.ramon.almacen.dto.reportes.ReporteVentasSucursalResponse(
            s.id,
            s.nombre,
            SUM(d.precioProducto * d.cantidadProducto),
            SUM(d.cantidadProducto)
        )
        FROM Venta v
        JOIN v.sucursal s
        JOIN v.detalleVentas d
        WHERE v.estadoVenta = :estado
        GROUP BY s.id, s.nombre
    """)
    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal(@Param("estado") EstadoVenta estado);
}