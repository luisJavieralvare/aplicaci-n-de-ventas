package com.rutasjj.sistema.config;

import com.rutasjj.sistema.model.Rol;
import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.RolRepository;
import com.rutasjj.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear rol ADMIN si no existe
        Rol adminRole = rolRepository.findByNombre("ADMIN");
        if (adminRole == null) {
            adminRole = new Rol();
            adminRole.setNombre("ADMIN");
            rolRepository.save(adminRole);
        }

        // Crear rol TRABAJADOR si no existe
        Rol trabajadorRole = rolRepository.findByNombre("TRABAJADOR");
        if (trabajadorRole == null) {
            trabajadorRole = new Rol();
            trabajadorRole.setNombre("TRABAJADOR");
            rolRepository.save(trabajadorRole);
        }

        // Crear tu usuario administrador si no existe
        if (usuarioRepository.findByCorreo("sanchezjaviis18@gmail.com") == null) {
            Usuario adminUser = new Usuario();
            adminUser.setNombre("luis javier");
            adminUser.setCorreo("sanchezjaviis18@gmail.com");
            adminUser.setContrasena(passwordEncoder.encode("adminjavier")); // Contraseña cifrada
            adminUser.setRol(adminRole);
            
            // Un admin debe estar aprobado por defecto
            adminUser.setEstado(Usuario.EstadoCuenta.APROBADO); 

            usuarioRepository.save(adminUser);
            System.out.println(">>>>>>>> Usuario administrador 'luis javier' creado exitosamente <<<<<<<<");
        }
    }
}