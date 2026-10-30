package com.ramon.almacen.enums;

import com.ramon.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Categoria {
    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETE("Juguete"),
    ELECTRONICA("Electronica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FARMACIA("Farmacia");

    private final String descripcion;

    public static Categoria obtenerCategoriaPorDescripcion(String descripcion){
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return null;
        }

        String descriocionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for(Categoria categoria : values()){
            // Comparamos de forma segura normalizando también la descripción del enum
            if(StringCustomUtils.normalizarTexto(categoria.descripcion).equalsIgnoreCase(descriocionNormalizada)) {
                return categoria;
            }
        }

        // En lugar de tronar la app con una excepción en un filtro, retornamos null
        return null;
    }
}
