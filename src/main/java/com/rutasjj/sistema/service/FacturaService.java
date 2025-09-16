package com.rutasjj.sistema.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;  
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacturaService {

    public ByteArrayInputStream generarFactura(String trabajador, List<ProductoFactura> productos) {
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
            document.add(new Paragraph("Trabajador: " + trabajador));
            document.add(new Paragraph("Fecha: " + LocalDateTime.now()));
            document.add(new Paragraph(" "));

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4, 2, 2, 2});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Producto", headFont));
            tabla.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Cantidad", headFont));
            tabla.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Precio", headFont));
            tabla.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Subtotal", headFont));
            tabla.addCell(hcell);

            double total = 0;
            for (ProductoFactura p : productos) {
                tabla.addCell(p.getNombre());
                tabla.addCell(String.valueOf(p.getCantidad()));
                tabla.addCell(String.format("$%.2f", p.getPrecio()));
                double subtotal = p.getCantidad() * p.getPrecio();
                tabla.addCell(String.format("$%.2f", subtotal));
                total += subtotal;
            }

            document.add(tabla);

            document.add(new Paragraph(" "));
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph totalParrafo = new Paragraph("Total: $" + String.format("%.2f", total), fontTotal);
            totalParrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParrafo);

            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
