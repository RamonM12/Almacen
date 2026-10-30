package com.ramon.almacen.controllers;

import com.ramon.almacen.docs.ProblemaDoc;
import com.ramon.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.ramon.almacen.dto.ventas.VentaRequest;
import com.ramon.almacen.dto.ventas.VentaResponse;
import com.ramon.almacen.services.reportes.ReporteService;
import com.ramon.almacen.services.ventas.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Endpoints para la gestión de ventas y stock")
// Errores globales para todos los endpoints de ventas:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class VentaController {

    private final VentaService ventaService;
    private final ReporteService reporteService;

    @PostMapping
    @Operation(summary = "Registrar una nueva venta", description = "Realiza la validación de stock, bloqueo pesimista, fotografía de precio y descuento atómico.")
    @ApiResponse(responseCode = "201", description = "Venta registrada con éxito")
    @ApiResponse(responseCode = "409", description = "Conflicto de stock o regla de negocio violada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest request) {
        VentaResponse ventaRegistrada = ventaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaRegistrada);
    }

    @GetMapping
    @Operation(summary = "Listar ventas activas", description = "Retorna únicamente las ventas en estado REGISTRADA.")
    @ApiResponse(responseCode = "200", description = "Listado de ventas activas obtenido")
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta activa por ID", description = "Busca una venta específica que se encuentre registrada.")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "La venta no existe o no está activa",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorId(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }

    @GetMapping("/reportes/financiero/sucursales")
    @Operation(summary = "Reporte financiero por sucursal", description = "Obtiene el rendimiento económico y de piezas agrupado por sucursal.")
    @ApiResponse(responseCode = "200", description = "Reporte financiero generado con éxito")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReporteFinancieroSucursales() {
        return ResponseEntity.ok(reporteService.obtenerReporteRendimientoSucursales());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar una venta", description = "Realiza un borrado lógico cambiando el estado a cancelada y devolviendo stock al inventario sin eliminar el registro físicamente.")
    @ApiResponse(responseCode = "204", description = "Venta cancelada exitosamente")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La venta ya se encontraba cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID de la venta a cancelar", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        ventaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/canceladas")
    @Operation(summary = "Histórico de ventas canceladas", description = "Endpoint exclusivo para consultar el registro de todas las ventas que fueron canceladas.")
    @ApiResponse(responseCode = "200", description = "Listado de ventas canceladas obtenido")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }
}