package com.rutasjj.sistema.service;

import com.rutasjj.sistema.model.Mercancia;
import com.rutasjj.sistema.repository.MercanciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MercanciaService {

    @Autowired
    private MercanciaRepository mercanciaRepository;

    @Autowired
    private ProductoService productoService;

    @Transactional
    public void registrarMercancia(Mercancia mercancia) {
        // Guardar el registro de mercancía
        mercanciaRepository.save(mercancia);

        // Actualizar el stock del producto según el tipo de registro
        if (mercancia.getTipoRegistro() == Mercancia.TipoRegistro.LLEVADA) {
            productoService.disminuirStock(mercancia.getProducto().getId(), mercancia.getCantidad());
        } else if (mercancia.getTipoRegistro() == Mercancia.TipoRegistro.DEVUELTA) {
            productoService.aumentarStock(mercancia.getProducto().getId(), mercancia.getCantidad());
        }
    }
}