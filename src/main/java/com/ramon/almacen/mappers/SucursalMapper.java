package com.ramon.almacen.mappers;

import com.ramon.almacen.entities.Sucursal;
import com.ramon.almacen.services.sucursales.SucursalRequest;
import com.ramon.almacen.services.sucursales.SucursalResponse;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {
    public Sucursal requestAEntidad(SucursalRequest request){

        return request == null
                ? null
                : Sucursal.crear(
                request.nombre(),
                request.direccion()
        );
    }

    public SucursalResponse entidadAResponse(Sucursal sucursal){

        return sucursal == null
                ? null
                : new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }
}

