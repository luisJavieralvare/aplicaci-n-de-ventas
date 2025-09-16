package com.rutasjj.sistema.service;

import com.rutasjj.sistema.model.Factura;
import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.FacturaRepository;
import com.rutasjj.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Map<String, Object> getAdminDashboardData() {
        Map<String, Object> data = new HashMap<>();
        LocalDate hoy = LocalDate.now();

        // Calcular ventas totales del día
        List<Factura> facturasHoy = facturaRepository.findAllByFechaBetween(hoy, hoy);
        BigDecimal ventasTotalesHoy = facturasHoy.stream()
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Contar registros pendientes de aprobación
        long usuariosPendientes = usuarioRepository.countByEstado(Usuario.EstadoCuenta.PENDIENTE);

        data.put("ventasTotalesHoy", ventasTotalesHoy);
        data.put("usuariosPendientes", usuariosPendientes);
        return data;
    }

    public Map<String, Object> getTrabajadorDashboardData(Integer trabajadorId) {
        Map<String, Object> data = new HashMap<>();
        LocalDate hoy = LocalDate.now();

        // Calcular ventas del trabajador para el día de hoy
        List<Factura> facturasHoy = facturaRepository.findByTrabajadorIdAndFecha(trabajadorId, hoy);
        BigDecimal misVentasHoy = facturasHoy.stream()
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        data.put("misVentasHoy", misVentasHoy);
        return data;
    }
}