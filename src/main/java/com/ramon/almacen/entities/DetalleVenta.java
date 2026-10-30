package com.ramon.almacen.entities;

import com.ramon.almacen.exceptions.DatoInvalidoException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLES_VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "Entidad que representa el detalle de un producto dentro de una venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_VENTA")
    @Schema(description = "ID único del detalle de venta", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENTA")
    @Schema(description = "Venta asociada a este detalle")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO")
    @Schema(description = "Producto asociado al detalle de venta")
    private Producto producto;

    @Column(name = "CANTIDAD_PRODUCTO", nullable = false) // Nota: corregido de @JoinColumn a @Column ya que es un atributo escalar
    @Schema(description = "Cantidad de unidades vendidas del producto", example = "5")
    private Integer cantidadProducto;

    @Column(name = "PRECIO_PRODUCTO", nullable = false) // Nota: corregido de @JoinColumn a @Column ya que es un atributo escalar
    @Schema(description = "Precio unitario del producto al momento de la venta (fotografía de precio)", example = "1500.00")
    private BigDecimal precioProducto;

    public void asignarVenta(Venta venta){
        if(venta == null)
            throw new DatoInvalidoException("La venta es requerida");

        this.venta = venta;
    }
}