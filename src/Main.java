import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.RandomStringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Stream;

public class Main {

    public static String folderPath = "C:\\Users\\ACER\\Documents\\project files\\text-pdf\\";
    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        System.out.println("Hello world!");
        var doc = new Document();
        String filename = folderPath + (generateRandomString() + ".pdf").toString();
        PdfWriter.getInstance(doc, new FileOutputStream(filename));
        doc.open();
        var bold = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        var paragraph = new Paragraph("Testing for questionnaire");
        var table = new PdfPTable(2);
        Stream.of("Question", "Answer").forEach(table::addCell);
        Arrays.stream(ChronoUnit.values())
                .forEach(val -> {
                    table.addCell(val.toString());
                    table.addCell(val.getDuration().toString());
                });
        paragraph.add(table);
        doc.add(paragraph);
        doc.close();
    }

    public static String generateRandomString() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);

        System.out.println(generatedString);

        return generatedString;
    }
}