// SucursalRequest.java (Debe llevar las validaciones)
package com.ramon.almacen.services.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear o actualizar una sucursal")
public record SucursalRequest(
        @Schema(description = "Nombre de la sucursal", example = "Sucursal Central")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 50, message = "El nombre debe tener entre 5 y 50 caracteres")
        String nombre,

        @Schema(description = "Dirección de la sucursal", example = "Av. Principal 123")
        @NotBlank(message = "La dirección es requerida")
        @Size(min = 5, max = 150, message = "La dirección debe tener entre 5 y 150 caracteres")
        String direccion
) {}