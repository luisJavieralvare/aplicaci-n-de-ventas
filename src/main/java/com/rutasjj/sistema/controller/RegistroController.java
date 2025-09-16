package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Rol;
import com.rutasjj.sistema.model.Trabajador;
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
        // 1. Crear la entidad Trabajador a partir de los datos del usuario
        Trabajador nuevoTrabajador = new Trabajador();
        nuevoTrabajador.setNombre(usuario.getNombre());
        nuevoTrabajador.setDisponible(true);

        // 2. Vincular el nuevo Trabajador con el Usuario
        usuario.setTrabajador(nuevoTrabajador);

        // 3. El resto de la lógica para guardar el usuario
        Rol rol = rolRepository.findByNombre("TRABAJADOR");
        usuario.setRol(rol);
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setEstado(Usuario.EstadoCuenta.PENDIENTE);
        
        // Al guardar el usuario, JPA guardará también el trabajador asociado gracias a CascadeType.ALL
        usuarioRepository.save(usuario);
        
        return "redirect:/login?registered";
    }
}