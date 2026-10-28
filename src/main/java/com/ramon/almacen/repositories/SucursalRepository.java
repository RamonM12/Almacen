package com.ramon.almacen.repositories;

import com.ramon.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    //SELECT * FROM SUCURSALES WHERE NOMBRE = NOMBRE
    boolean existsByNombreIgnoreCase(String nombre);

    //SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(:NOMBRE) AND ID_SUCURSAL <>:ID;
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
