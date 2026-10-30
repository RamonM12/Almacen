package com.ramon.almacen.entities;

import com.ramon.almacen.enums.EstadoVenta;
import com.ramon.almacen.exceptions.ConflictException;
import com.ramon.almacen.exceptions.DatoInvalidoException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "Entidad que representa una venta realizada en el almacén")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    @Schema(description = "ID único de la venta", example = "1")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de la venta", example = "REGISTRADA")
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    @Schema(description = "Fecha en la que se realizó la venta", example = "2026-10-29")
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    @Schema(description = "Sucursal donde se efectuó la venta")
    private Sucursal sucursal;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta", cascade = CascadeType.ALL)
    @Schema(description = "Lista de detalles de los productos incluidos en la venta")
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

    public void agregarDetalle(DetalleVenta detalleVenta){
        if(detalleVenta == null)
            throw new DatoInvalidoException("El detalle de la venta es requerido");

        this.detalleVentas.add(detalleVenta);
        detalleVenta.asignarVenta(this);
    }

    public void cancelar(){
        if(this.estadoVenta == EstadoVenta.CANCELADA)
            throw new ConflictException("La venta ya esta cancelada");

        this.estadoVenta = EstadoVenta.CANCELADA;
    }

    public BigDecimal getTotal() {
        if (detalleVentas == null || detalleVentas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return detalleVentas.stream()
                .map(detalle -> detalle.getPrecioProducto().multiply(BigDecimal.valueOf(detalle.getCantidadProducto())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}