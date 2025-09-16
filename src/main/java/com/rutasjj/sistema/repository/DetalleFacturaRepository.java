package com.rutasjj.sistema.repository;

import com.rutasjj.sistema.model.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Integer> {
}