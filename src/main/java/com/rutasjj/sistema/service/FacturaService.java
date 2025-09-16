package com.rutasjj.sistema.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.rutasjj.sistema.model.*;
import com.rutasjj.sistema.repository.FacturaRepository;
import com.rutasjj.sistema.repository.MercanciaRepository;
import com.rutasjj.sistema.repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private MercanciaRepository mercanciaRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Transactional
    public Factura generarYGuardarFactura(Integer idTrabajador, LocalDate fecha) {
        // 1. Obtener datos necesarios
        Trabajador trabajador = trabajadorRepository.findById(idTrabajador)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
        List<Mercancia> mercanciasDelDia = mercanciaRepository.findByTrabajadorIdAndFecha(idTrabajador, fecha);

        // 2. Calcular cantidades vendidas
        Map<Producto, Integer> llevadaMap = mercanciasDelDia.stream()
                .filter(m -> m.getTipoRegistro() == Mercancia.TipoRegistro.LLEVADA)
                .collect(Collectors.groupingBy(Mercancia::getProducto, Collectors.summingInt(Mercancia::getCantidad)));

        Map<Producto, Integer> devueltaMap = mercanciasDelDia.stream()
                .filter(m -> m.getTipoRegistro() == Mercancia.TipoRegistro.DEVUELTA)
                .collect(Collectors.groupingBy(Mercancia::getProducto, Collectors.summingInt(Mercancia::getCantidad)));

        BigDecimal totalFactura = BigDecimal.ZERO;
        List<DetalleFactura> detalles = new ArrayList<>();

        // 3. Crear la factura y sus detalles
        Factura factura = new Factura();
        factura.setTrabajador(trabajador);
        factura.setFecha(fecha);

        for (Producto producto : llevadaMap.keySet()) {
            int cantidadLlevada = llevadaMap.getOrDefault(producto, 0);
            int cantidadDevuelta = devueltaMap.getOrDefault(producto, 0);
            int cantidadVendida = cantidadLlevada - cantidadDevuelta;

            if (cantidadVendida > 0) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setProducto(producto);
                detalle.setCantidadVendida(cantidadVendida);
                detalle.setPrecioUnitario(producto.getPrecio());
                BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(cantidadVendida));
                detalle.setSubtotal(subtotal);
                detalle.setFactura(factura);

                detalles.add(detalle);
                totalFactura = totalFactura.add(subtotal);
            }
        }
        
        if (detalles.isEmpty()) {
            throw new RuntimeException("No se registraron ventas para este trabajador en la fecha especificada.");
        }

        factura.setTotal(totalFactura);
        factura.setDetalles(detalles);

        // 4. Guardar la factura en la base de datos
        return facturaRepository.save(factura);
    }

    public ByteArrayInputStream generarPdfDeFactura(Factura factura) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Encabezado
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Factura - Rutas J-J", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Trabajador: " + factura.getTrabajador().getNombre()));
            document.add(new Paragraph("Fecha: " + factura.getFecha()));
            document.add(new Paragraph("Factura ID: " + factura.getId()));
            document.add(new Paragraph(" "));

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4, 2, 2, 2});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            
            // Celdas del encabezado de la tabla
            tabla.addCell(new PdfPCell(new Phrase("Producto", headFont)));
            tabla.addCell(new PdfPCell(new Phrase("Cantidad Vendida", headFont)));
            tabla.addCell(new PdfPCell(new Phrase("Precio Unitario", headFont)));
            tabla.addCell(new PdfPCell(new Phrase("Subtotal", headFont)));
            
            // Llenar la tabla con los detalles de la factura
            for (DetalleFactura detalle : factura.getDetalles()) {
                tabla.addCell(detalle.getProducto().getNombre());
                tabla.addCell(String.valueOf(detalle.getCantidadVendida()));
                tabla.addCell(String.format("$%.2f", detalle.getPrecioUnitario()));
                tabla.addCell(String.format("$%.2f", detalle.getSubtotal()));
            }

            document.add(tabla);

            document.add(new Paragraph(" "));
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph totalParrafo = new Paragraph("Total: $" + String.format("%.2f", factura.getTotal()), fontTotal);
            totalParrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParrafo);

            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}