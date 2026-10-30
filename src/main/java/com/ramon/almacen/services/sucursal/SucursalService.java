package com.ramon.almacen.services.sucursal;

import com.ramon.almacen.services.sucursales.SucursalRequest;
import com.ramon.almacen.services.sucursales.SucursalResponse;

import java.util.List;


public interface SucursalService {

    List<SucursalResponse> listar();

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);

}