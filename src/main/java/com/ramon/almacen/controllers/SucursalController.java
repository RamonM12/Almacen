package com.ramon.almacen.controllers;

import com.ramon.almacen.docs.ProblemaDoc;
import com.ramon.almacen.services.sucursal.SucursalService;
import com.ramon.almacen.services.sucursales.SucursalRequest;
import com.ramon.almacen.services.sucursales.SucursalResponse;
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
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "API Sucursales", description = "Gestion de Sucursales")

//Errores globales
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class SucursalController {
    private final SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Obtiene todas las sucursales registradas")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<SucursalResponse>> listar (
    ){
        return ResponseEntity.ok(sucursalService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada")
    public ResponseEntity<SucursalResponse> obtenerPorId(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva sucursal")
    @ApiResponse(responseCode = "201", description = "Sucursal creada")
    public ResponseEntity<SucursalResponse> registrar(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sucursalService.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal existente")
    @ApiResponse(responseCode = "200", description = "Sucursal actualizada")
    public ResponseEntity<SucursalResponse> actualizar(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody SucursalRequest request
    ) {
        return ResponseEntity.ok(sucursalService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sucursal")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
