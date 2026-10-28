package com.ramon.almacen.entities;

import com.ramon.almacen.enums.Categoria;
import com.ramon.almacen.exceptions.DatoInvalidoException;
import com.ramon.almacen.utils.StringCustomUtils;
import com.ramon.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Table(name = "PRODUCTOS")
@NoArgsConstructor//Contructor vacio
@AllArgsConstructor//Todos los constructores
@Getter @Builder

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 30)
    private String nombre;

    @Column(name = "CATEGORIA", nullable = false)
    @Enumerated(EnumType.STRING)//Es para seleccionar el valor de la lista en vez del puesto en numero
    private Categoria categoria;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    public static void validarDatos(
            String nombre,
            Categoria categoria,
            BigDecimal precio,
            Integer cantidad) {

        StringCustomUtils.validarTamanio(nombre, 5, 30,
                "El nombre es requerido y debe tener entre 5 y 30 caracteres");

        if(categoria == null)
            throw new DatoInvalidoException("La categoria es requerida");

        ValoresNumericosUtils.validarBigDecimalPositivo(precio,
                "La cantidad es requerida y debe ser positivo");

        ValoresNumericosUtils.validarEnteroPositivo(cantidad,
                "la cantidad requerida y debe ser positiva");
    }

    public void actualizar(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad) {

        this.nombre = nombre.trim();
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public void aumentarCantidad(int cantidad){
        ValoresNumericosUtils.validarEnteroPositivo(
                cantidad,
                "La cantidad debe ser positiva"
        );
        this.cantidad += cantidad;
    }

    public void descontarCantidad(int cantidad){
        ValoresNumericosUtils.validarEnteroPositivo(
                cantidad,
                "La cantidad debe ser negativa"
        );
        if(cantidad > this.cantidad)
            throw new DatoInvalidoException(
                    "La cantidad debe ser menor o "+ "igual a " +
                    "la cantidad actual");
        this.cantidad -= cantidad;
    }

    public static Producto crear(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad) {
       validarDatos(nombre, categoria,precio,cantidad);

       return Producto.builder()
               .nombre(nombre.trim())
               .categoria(categoria)
               .precio(precio)
               .cantidad(cantidad)
               .build();
    }
    //return new Producto(id: null, nombre, categoria, precio, cantidad); -> es lo mismo que el de arriba

}
