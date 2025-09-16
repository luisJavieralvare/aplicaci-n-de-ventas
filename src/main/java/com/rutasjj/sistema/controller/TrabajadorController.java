package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Trabajador;
import com.rutasjj.sistema.repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @GetMapping
    public List<Trabajador> listarTrabajadores() {
        return trabajadorRepository.findAll();
    }

    @PostMapping
    public Trabajador crearTrabajador(@RequestBody Trabajador trabajador) {
        return trabajadorRepository.save(trabajador);
    }
}