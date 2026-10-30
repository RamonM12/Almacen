// SucursalResponse.java (Solo los datos de salida)
package com.ramon.almacen.services.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de una sucursal")
public record SucursalResponse(
        @Schema(description = "Identificador de la sucursal", example = "1")
        Long id,
        @Schema(description = "Nombre de la sucursal", example = "Sucursal Central")
        String nombre,
        @Schema(description = "Dirección de la sucursal", example = "Av. Principal 123")
        String direccion
) {}