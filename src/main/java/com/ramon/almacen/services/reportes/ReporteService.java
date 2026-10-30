package com.ramon.almacen.services.reportes;

import com.ramon.almacen.dto.reportes.ReporteVentasSucursalResponse;

import java.util.List;

public interface ReporteService {
    List<ReporteVentasSucursalResponse> obtenerReporteRendimientoSucursales();
}
