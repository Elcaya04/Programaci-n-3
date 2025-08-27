package utilites;


import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;


public class PDF {
    public static void exportTable(JTable table, String filePath) throws Exception {
    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream(filePath));
    document.open();

    document.add(new Paragraph("Reporte de Médicos"));
    document.add(new Paragraph(" ")); // salto de línea

    TableModel model = table.getModel();
    PdfPTable pdfTable = new PdfPTable(model.getColumnCount());

    // Agregar encabezados
    for (int i = 0; i < model.getColumnCount(); i++) {
        pdfTable.addCell(new Phrase(model.getColumnName(i)));
    }

    // Agregar filas
    for (int row = 0; row < model.getRowCount(); row++) {
        for (int col = 0; col < model.getColumnCount(); col++) {
            Object value = model.getValueAt(row, col);
            pdfTable.addCell(value == null ? "" : value.toString());
        }
    }

    document.add(pdfTable);
    document.close();
}
}
