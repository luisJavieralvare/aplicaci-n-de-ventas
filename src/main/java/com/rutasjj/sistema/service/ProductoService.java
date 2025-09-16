package com.rutasjj.sistema.service;

import com.rutasjj.sistema.model.Producto;
import com.rutasjj.sistema.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public void disminuirStock(Integer idProducto, int cantidad) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    @Transactional
    public void aumentarStock(Integer idProducto, int cantidad) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }
}
