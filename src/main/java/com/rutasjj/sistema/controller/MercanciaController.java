package com.rutasjj.sistema.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rutasjj.sistema.model.Mercancia;
import com.rutasjj.sistema.model.Producto;
import com.rutasjj.sistema.model.Usuario;
import com.rutasjj.sistema.repository.MercanciaRepository;
import com.rutasjj.sistema.repository.ProductoRepository;
import com.rutasjj.sistema.repository.UsuarioRepository;
import com.rutasjj.sistema.service.MercanciaService;

@Controller
@RequestMapping("/mercancia")
public class MercanciaController {

    @Autowired
    private MercanciaService mercanciaService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MercanciaRepository mercanciaRepository; // ¡NUEVO!

    public static class MercanciaRequest {
        public Integer productoId;
        public Integer cantidad;
    }

    @GetMapping("/registro")
    public String mostrarFormularioMercancia(Model model, Authentication authentication) {
        // Obtenemos el trabajador actual
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        // Buscamos la mercancía que se llevó hoy
        List<Mercancia> mercanciaLlevada = mercanciaRepository.findByTrabajadorIdAndFechaAndTipoRegistro(
            usuario.getTrabajador().getId(), LocalDate.now(), Mercancia.TipoRegistro.LLEVADA);
            
        List<Producto> productosDisponibles = productoRepository.findAll();
        
        model.addAttribute("productos", productosDisponibles);
        model.addAttribute("mercanciaLlevada", mercanciaLlevada); // La enviamos a la vista
        return "registro-mercancia";
    }

    @PostMapping("/api/registrar-llevada") // Endpoint renombrado para claridad
    @ResponseBody
    public ResponseEntity<String> registrarMercanciaLlevada(@RequestBody MercanciaRequest request, Authentication authentication) {
        try {
            Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
            Producto producto = productoRepository.findById(request.productoId).orElseThrow();

            Mercancia mercancia = new Mercancia();
            mercancia.setProducto(producto);
            mercancia.setCantidad(request.cantidad);
            mercancia.setTrabajador(usuario.getTrabajador());
            mercancia.setFecha(LocalDate.now());
            mercancia.setTipoRegistro(Mercancia.TipoRegistro.LLEVADA);

            mercanciaService.registrarMercancia(mercancia);
            return ResponseEntity.ok("Mercancía LLEVADA registrada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ¡NUEVO ENDPOINT PARA DEVOLUCIONES!
    @PostMapping("/api/registrar-devolucion")
    @ResponseBody
    public ResponseEntity<String> registrarMercanciaDevuelta(@RequestBody MercanciaRequest request, Authentication authentication) {
        try {
            Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
            Producto producto = productoRepository.findById(request.productoId).orElseThrow();

            Mercancia mercancia = new Mercancia();
            mercancia.setProducto(producto);
            mercancia.setCantidad(request.cantidad);
            mercancia.setTrabajador(usuario.getTrabajador());
            mercancia.setFecha(LocalDate.now());
            mercancia.setTipoRegistro(Mercancia.TipoRegistro.DEVUELTA);

            mercanciaService.registrarMercancia(mercancia);
            return ResponseEntity.ok("Mercancía DEVUELTA registrada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}