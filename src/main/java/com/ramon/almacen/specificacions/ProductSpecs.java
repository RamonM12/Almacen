package com.ramon.almacen.specificacions;

import com.ramon.almacen.entities.Producto;
import com.ramon.almacen.enums.Categoria;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecs {

    public static Specification<Producto> conNombre(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.trim().isEmpty())
                return null;

            return cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Producto> conCategoria(String categoriaStr) {
        return (root, query, cb) -> {
            if (categoriaStr == null || categoriaStr.trim().isEmpty())
                return null;
            try {
                Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(categoriaStr.trim());
                return cb.equal(root.get("categoria"), categoria);
            } catch (Exception e) {
                return cb.disjunction();//Para que no truene cuando lo use el cliente
            }
        };
    }

    public static Specification<Producto> conPrecioMin(BigDecimal precioMin) {
        return (root, query, cb) ->{
            if (precioMin == null)
                return null;
            return cb.greaterThanOrEqualTo(root.get("precio"), precioMin);

        };
    }

    public static Specification<Producto> conPrecioMax(BigDecimal precioMax){
        return(root,query,cb)->{
            if(precioMax == null)
                return null;
            return cb.lessThanOrEqualTo(root.get("precio"),precioMax);
        };
    }
}