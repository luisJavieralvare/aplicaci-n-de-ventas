package com.rutasjj.sistema.controller;

import com.rutasjj.sistema.model.Categoria;
import com.rutasjj.sistema.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        model.addAttribute("nuevaCategoria", new Categoria());
        return "categorias"; // Este es el nombre del archivo HTML
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("nuevaCategoria") Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}