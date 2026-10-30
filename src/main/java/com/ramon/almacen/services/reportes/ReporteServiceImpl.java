package com.ramon.almacen.services.reportes;

import com.ramon.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.ramon.almacen.enums.EstadoVenta;
import com.ramon.almacen.repositories.VentasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentasRepository ventasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerReporteRendimientoSucursales() {
        return ventasRepository.obtenerReportePorSucursal(EstadoVenta.REGISTRADA);
    }
}