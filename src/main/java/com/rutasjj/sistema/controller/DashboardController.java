package com.rutasjj.sistema.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.UsuarioRepository;
import com.rutasjj.sistema.service.DashboardService;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        
        if (usuario.getRol().getNombre().equals("ADMIN")) {
            Map<String, Object> adminData = dashboardService.getAdminDashboardData();
            model.addAttribute("dashboardData", adminData);
        } else if (usuario.getRol().getNombre().equals("TRABAJADOR")) {
            Map<String, Object> trabajadorData = dashboardService.getTrabajadorDashboardData(usuario.getTrabajador().getId());
            model.addAttribute("dashboardData", trabajadorData);
        }
        
        return "dashboard";
    }
}