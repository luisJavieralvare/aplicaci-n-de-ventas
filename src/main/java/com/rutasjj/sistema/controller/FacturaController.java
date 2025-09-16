package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.service.FacturaService;
import com.rutasjj.sistema.service.ProductoFactura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

@RestController
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping("/factura")
    public ResponseEntity<byte[]> generarFactura() {
        // 🔹 Simulación de productos (esto luego se traerá de la BD)
        List<ProductoFactura> productos = Arrays.asList(
                new ProductoFactura("Arroz", 5, 2500),
                new ProductoFactura("Aceite", 2, 8000),
                new ProductoFactura("Azúcar", 3, 4000)
        );

        ByteArrayInputStream bis = facturaService.generarFactura("Juan Pérez", productos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}
