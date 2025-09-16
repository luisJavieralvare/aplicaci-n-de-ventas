package com.rutasjj.sistema.service;

import com.rutasjj.sistema.model.Factura;
import com.rutasjj.sistema.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private FacturaRepository facturaRepository;

    public Map<String, Object> generarReporteSemanal(LocalDate fechaReferencia) {
        // 1. Calcula el inicio (domingo) y fin (sábado) de la semana
        LocalDate inicioSemana = fechaReferencia.with(DayOfWeek.SUNDAY);
        LocalDate finSemana = inicioSemana.plusDays(6);

        // 2. Busca las facturas en ese rango de fechas
        List<Factura> facturas = facturaRepository.findAllByFechaBetween(inicioSemana, finSemana);

        // 3. Procesa los datos para el reporte
        double totalVentas = facturas.stream()
                                     .mapToDouble(f -> f.getTotal().doubleValue())
                                     .sum();

        // ¡NUEVO! Datos para el gráfico: ventas totales por día de la semana
        Map<DayOfWeek, Double> ventasPorDia = facturas.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getFecha().getDayOfWeek(),
                        Collectors.summingDouble(f -> f.getTotal().doubleValue())
                ));

        // 4. Prepara la respuesta
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("fechaInicio", inicioSemana.toString());
        reporte.put("fechaFin", finSemana.toString());
        reporte.put("totalVentasSemana", totalVentas);
        reporte.put("numeroFacturas", facturas.size());
        reporte.put("ventasPorDia", ventasPorDia); // Enviamos los datos para el gráfico

        return reporte;
    }
}