package com.ramon.almacen.services.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear o actualizar")
public record SucursalResponse(
        Long id, @Schema(description = "nombre de la sucursal", example = "Sucursal del norete")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 50, message = "El nombre debe tener 5 y 50 caracteres")
        String nombre,
        @Schema(description = "direccion de la sucursal", example = "Sucursal del norete")
        @NotBlank(message = "El direccion es requerido")
        @Size(min = 5, max = 50, message = "El direccion debe tener 5 y 50 caracteres")
        String direccion
) {
}
