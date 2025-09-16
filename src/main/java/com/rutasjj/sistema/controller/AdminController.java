package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Muestra la página con la lista de usuarios pendientes
    @GetMapping("/aprobaciones")
    public String mostrarAprobaciones(Model model) {
        List<Usuario> usuariosPendientes = usuarioRepository.findByEstado(Usuario.EstadoCuenta.PENDIENTE);
        model.addAttribute("usuarios", usuariosPendientes);
        return "admin/aprobaciones"; // Busca el archivo en templates/admin/aprobaciones.html
    }

    // Procesa la aprobación de un usuario
    @PostMapping("/usuarios/aprobar/{id}")
    public String aprobarUsuario(@PathVariable("id") Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido:" + id));
        
        usuario.setEstado(Usuario.EstadoCuenta.APROBADO);
        usuarioRepository.save(usuario);
        
        return "redirect:/admin/aprobaciones";
    }
}