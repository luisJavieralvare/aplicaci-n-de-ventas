package com.rutasjj.sistema.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rutasjj.sistema.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findAllByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Factura> findByTrabajadorIdAndFecha(Integer trabajadorId, LocalDate fecha);
    List<Factura> findByTrabajadorIdOrderByFechaDesc(Integer trabajadorId);
}


