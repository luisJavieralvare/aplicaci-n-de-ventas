package com.rutasjj.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rutasjj.sistema.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}