package com.ramon.almacen.enums;

import com.ramon.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Categoria {
    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETE("Jueguete"),
    ELECTRONICA("Electronica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FARMACIA("Farmacia");

    private final String descripcion;

    public static Categoria obtenerCategoriaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descriocionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for(Categoria categoria : values()){
            if(StringCustomUtils.normalizarTexto(categoria.descripcion).equalsIgnoreCase(descriocionNormalizada))
                return categoria;
        }

        throw new RuntimeException("No existe una categoria con la descripcion: " + descripcion);
    }
}
