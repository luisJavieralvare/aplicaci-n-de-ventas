package com.rutasjj.sistema.repository;

import com.rutasjj.sistema.model.Mercancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MercanciaRepository extends JpaRepository<Mercancia, Integer> {
    // Este método será crucial para encontrar la mercancía de un trabajador en una fecha específica
    List<Mercancia> findByTrabajadorIdAndFecha(Integer idTrabajador, LocalDate fecha);
}