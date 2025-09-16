package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Mercancia;
import com.rutasjj.sistema.service.MercanciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mercancia")
public class MercanciaController {

    @Autowired
    private MercanciaService mercanciaService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarMercancia(@RequestBody Mercancia mercancia) {
        try {
            mercanciaService.registrarMercancia(mercancia);
            return ResponseEntity.ok("Registro de mercancía exitoso. Stock actualizado.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}