package com.ramon.almacen.services.ventas;

import com.ramon.almacen.dto.ventas.VentaRequest;
import com.ramon.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {
    List<VentaResponse> listar();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    void cancelar(Long id);

    List<VentaResponse> listarCanceladas();
}
