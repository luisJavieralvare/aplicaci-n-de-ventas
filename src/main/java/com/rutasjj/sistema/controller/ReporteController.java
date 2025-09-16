package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // ¡Cambio importante aquí!
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; // Y aquí

import java.time.LocalDate;
import java.util.Map;

@Controller // 1. Cambiamos @RestController por @Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // 2. AÑADIMOS ESTE MÉTODO para mostrar la página HTML
    @GetMapping
    public String paginaReportes() {
        return "reportes"; // Esto le dice a Spring que busque y muestre reportes.html
    }

    // 3. TU MÉTODO ACTUAL lo ajustamos para que sea el endpoint de la API
    @GetMapping("/api/semanal")
    @ResponseBody // Esta anotación asegura que este método devuelva JSON
    public ResponseEntity<Map<String, Object>> getReporteSemanal(
            @RequestParam("fecha") String fechaStr) {
        
        LocalDate fechaReferencia = LocalDate.parse(fechaStr);
        Map<String, Object> reporte = reporteService.generarReporteSemanal(fechaReferencia);
        return ResponseEntity.ok(reporte);
    }
}