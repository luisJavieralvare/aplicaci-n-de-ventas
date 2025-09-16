package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Categoria;
import com.rutasjj.sistema.model.Producto;
import com.rutasjj.sistema.repository.CategoriaRepository;
import com.rutasjj.sistema.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Muestra la lista de productos y el formulario para agregar uno nuevo
    @GetMapping
    public String listar(Model model) {
        List<Producto> productos = productoRepository.findAll();
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("nuevoProducto", new Producto());
        return "productos";
    }

    // Guarda un nuevo producto
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("nuevoProducto") Producto producto) {
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // Muestra el formulario para editar un producto
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        return "editar-producto";
    }

    // Actualiza un producto
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("producto") Producto producto) {
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // Elimina un producto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/productos";
    }
}