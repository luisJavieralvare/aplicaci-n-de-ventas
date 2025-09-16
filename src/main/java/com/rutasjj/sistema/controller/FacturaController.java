package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Factura;
import com.rutasjj.sistema.repository.FacturaRepository;
import com.rutasjj.sistema.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaRepository facturaRepository;

    // DTO simple para el cuerpo de la solicitud de generación de factura
    public static class GenerarFacturaRequest {
        public Integer id_trabajador;
        public String fecha; // Formato YYYY-MM-DD
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody GenerarFacturaRequest request) {
        try {
            LocalDate fecha = LocalDate.parse(request.fecha);
            Factura facturaGuardada = facturaService.generarYGuardarFactura(request.id_trabajador, fecha);
            
            // Devuelve un resumen de la factura creada
            return ResponseEntity.ok(Map.of(
                "id_factura", facturaGuardada.getId(),
                "total", facturaGuardada.getTotal(),
                "mensaje", "Factura generada y guardada correctamente."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPdf(@PathVariable Integer id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con el ID: " + id));

        ByteArrayInputStream bis = facturaService.generarPdfDeFactura(factura);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}