package com.rutasjj.sistema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rutasjj.sistema.model.Factura;
import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.FacturaRepository;
import com.rutasjj.sistema.repository.UsuarioRepository;

@Controller
@RequestMapping("/trabajador")
public class TrabajadorViewController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @GetMapping("/mis-facturas")
    public String mostrarMisFacturas(Model model, Authentication authentication) {
        // 1. Obtener el usuario que ha iniciado sesión
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        // 2. Buscar todas las facturas de ese trabajador
        List<Factura> misFacturas = facturaRepository.findByTrabajadorIdOrderByFechaDesc(usuario.getTrabajador().getId());

        // 3. Enviar la lista de facturas a la página
        model.addAttribute("facturas", misFacturas);

        // 4. Mostrar la página HTML
        return "trabajador/mis-facturas";
    }
}