package com.ramon.almacen.services.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de una sucursal")
public record SucursalRequest(
        @Schema(description = "Identificador de la sucursal", example = "")
        Long id,

        @Schema(description = "Identificador de la sucursal", example = "")
        String nombre,

        @Schema(description = "Identificador de la sucursal", example = "")
        String direccion
) {



}
