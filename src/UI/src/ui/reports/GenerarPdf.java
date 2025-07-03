package UI.src.ui.reports;

import UI.src.ui.utils.Util;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import reports.ReportVets;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GenerarPdf {
    public static void generarReporteVeterinarios(List<ReportVets> veterinarios) {
        try {
            // 1. Ruta de la carpeta y archivo
            String home = System.getProperty("user.home");
            String rutaCarpeta = home + "/Documentos/Reportes";
            File carpeta = new File(rutaCarpeta);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            // 2. Nombre del archivo con fecha y hora
            LocalDateTime ahora = LocalDateTime.now();
            String fechaHora = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "ReporteVeterinarios_" + fechaHora + ".pdf";
            String rutaCompleta = rutaCarpeta + "/" + nombreArchivo;

            // 3. Crear PDF
            PdfWriter writer = new PdfWriter(rutaCompleta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 4. Encabezado
            document.add(new Paragraph("Refugio de Animales").setBold().setFontSize(16));
            document.add(new Paragraph("Reporte de Veterinarios con Contrato").setItalic().setFontSize(14));
            document.add(new Paragraph("\n"));

            // 5. Tabla
            Table tabla = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2,2,2,2,2,2})).useAllAvailableWidth();
            tabla.addCell("Nombre Proveedor");
            tabla.addCell("Dirección");
            tabla.addCell("Provincia");
            tabla.addCell("especialidad");
            tabla.addCell("Clinica");
            tabla.addCell("Fecha Inicio");
            tabla.addCell("Fecha Fin");
            tabla.addCell("Fecha Conciliacion");
            tabla.addCell("Descripcion");

            for (ReportVets vet : veterinarios) {
                tabla.addCell(vet.nombre_proveedor());
                tabla.addCell(vet.direccion());
                tabla.addCell(vet.provincia());
                tabla.addCell(vet.especialidad());
                tabla.addCell(vet.clinica());
                tabla.addCell(Util.formatFecha(vet.fechaInicio()));
                tabla.addCell(Util.formatFecha(vet.fechaFin()));
                tabla.addCell(Util.formatFecha(vet.fechaConc()));
                tabla.addCell(vet.descripc());
            }

            document.add(tabla);
            document.close();
            System.out.println("PDF guardado en: " + rutaCompleta);

        } catch (FileNotFoundException e) {
            System.err.println("Error al crear el PDF: " + e.getMessage());
        }
    }
}
