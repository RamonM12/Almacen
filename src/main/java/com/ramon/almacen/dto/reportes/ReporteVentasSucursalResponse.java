package com.ramon.almacen.dto.reportes;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Reporte económico de rendimiento por sucursal")
public record ReporteVentasSucursalResponse(
        @Schema(description = "ID de la sucursal", example = "1")
        Long idSucursal,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
        String nombreSucursal,

        @Schema(description = "Total facturado de ventas activas", example = "15999.99")
        BigDecimal totalFacturado,

        @Schema(description = "Cantidad total de piezas vendidas", example = "45")
        Long cantidadProductosVendidos
) {
}