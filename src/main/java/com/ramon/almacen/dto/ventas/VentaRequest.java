package com.ramon.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record VentaRequest(
        @Schema(description = "ID de la sucursal", example = "1")
        @NotNull(message = "El ID de la sucursal es requerida")
        @Positive(message = "El ID de la sucursal debe ser positivo")
        Long idSucursal,

        @Schema(description = "Lista de producto de venta", example = "1")
        @NotEmpty(message = "La lista del producto es requerida y no debe estar vacia")
        List<@Valid  DetalleVentaRequest> productos
) {
}
