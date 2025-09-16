package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Rol;
import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.RolRepository;
import com.rutasjj.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping
    public String registrarUsuario(Usuario usuario) {
        // Asigna el rol de trabajador por defecto
        Rol rol = rolRepository.findByNombre("TRABAJADOR");
        usuario.setRol(rol);
        // Cifra la contraseña antes de guardarla
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return "redirect:/login"; // Redirige al login después del registro
    }
}