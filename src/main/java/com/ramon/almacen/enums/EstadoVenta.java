package com.ramon.almacen.enums;

import com.ramon.almacen.exceptions.DatoInvalidoException;
import com.ramon.almacen.utils.StringCustomUtils;
import com.ramon.almacen.utils.ValoresNumericosUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Schema(description = "Estados posibles de una venta en el sistema", allowableValues = {"REGISTRADA", "CANCELADA"})
public enum EstadoVenta {

    @Schema(description = "Venta activa y registrada exitosamente")
    REGISTRADA (1L, "Registrada"),

    @Schema(description = "Venta cancelada y stock devuelto al inventario")
    CANCELADA (0L, "Cancelada");

    private final Long codigo;

    private final String descripcion;

    public static EstadoVenta obtenerEstadoVentaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (EstadoVenta estadoVenta : values()) {
            if (StringCustomUtils.normalizarTexto(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }

        throw new DatoInvalidoException("No existe un estado de venta con la descripcion: " + descripcion);
    }

    public static EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo){
        ValoresNumericosUtils.validarNumeroRequerido(codigo, "El código es requerido");

        for (EstadoVenta estadoVenta : values()) {
            if (estadoVenta.codigo.equals(codigo))
                return estadoVenta;
        }

        throw new DatoInvalidoException("No existe un estado de venta con el codigo: " + codigo);
    }
}