package com.rutasjj.sistema.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rutasjj.sistema.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByCorreo(String correo);
    List<Usuario> findByEstado(Usuario.EstadoCuenta estado);
    long countByEstado(Usuario.EstadoCuenta estado);
}