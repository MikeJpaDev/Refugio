package UI.src.ui;

import UI.src.ui.utils.Util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import reports.ReportVets;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GenPdf {
    public static void exportTableToPDF(JTable table, String filePath)
            throws DocumentException, IOException {

        // Crear el documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Crear una tabla PDF con el mismo número de columnas que el JTable
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

        // Añadir encabezados de la tabla
        for (int i = 0; i < table.getColumnCount(); i++) {
            String header = table.getColumnModel().getColumn(i).getHeaderValue().toString();
            pdfTable.addCell(header);
        }

        // Ajustar el ancho de la tabla para que ocupe el 100% del ancho de página
        pdfTable.setWidthPercentage(100);

        // Añadir filas de datos
        TableModel model = table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                pdfTable.addCell(value != null ? value.toString() : "");
            }
        }

        // Añadir la tabla al documento
        document.add(pdfTable);
        document.close();
    }
}
