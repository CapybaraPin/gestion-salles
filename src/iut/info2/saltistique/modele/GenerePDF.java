package iut.info2.saltistique.modele;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Cette classe permet de générer un fichier PDF à partir d'un {@link TableView}
 * en y incluant des informations générales, un tableau dynamique et un en-tête personnalisé.
 */
public class GenerePDF {

    /**
     * Génère un fichier PDF à partir d'un {@link TableView} donné et l'ouvre automatiquement.
     *
     * @param tableView Le {@link TableView} contenant les données à exporter au format PDF.
     * @param pdfName   Le nom du fichier PDF (sans l'extension .pdf).
     * @param <T>       Le type des éléments contenus dans les lignes du {@link TableView}.
     * @throws IOException              Si une erreur survient lors de l'écriture du fichier PDF.
     * @throws IllegalArgumentException Si le {@link TableView} est null ou vide, ou si le nom du fichier est vide.
     */
    public static <T> void generateAndOpenPdf(TableView<T> tableView, String pdfName) throws IOException {
        if (tableView == null || tableView.getItems().isEmpty()) {
            throw new IllegalArgumentException("Le tableau est vide ou null.");
        }

        if (pdfName == null || pdfName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fichier PDF ne peut pas être vide.");
        }

        // Format pour le nom de fichier
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter fileDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedFileDate = currentDate.format(fileDateFormatter);

        // Format pour l'affichage dans le PDF
        DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDisplayDate = currentDate.format(displayDateFormatter);

        // Construire le chemin complet du fichier PDF
        String downloadsDir = System.getProperty("user.home") + File.separator + "Downloads";
        String fileName = pdfName.trim() + formattedFileDate + ".pdf";
        String dest = downloadsDir + File.separator + fileName;

        // Création du PDF
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        addHeader(document);
        addDocumentInfo(document, pdfName, formattedFileDate, formattedDisplayDate);

        Table pdfTable = createTableFromTableView(tableView);
        document.add(pdfTable);

        document.close();

        openPdf(dest);
    }

    /**
     * Crée un tableau PDF à partir des données et des colonnes d'un {@link TableView}.
     */
    public static <T> Table createTableFromTableView(TableView<T> tableView) {
        int numColumns = tableView.getColumns().size();
        Table table = new Table(UnitValue.createPercentArray(numColumns));
        table.setWidth(UnitValue.createPercentValue(100));

        for (TableColumn<T, ?> column : tableView.getColumns()) {
            table.addCell(new Cell().add(new Paragraph(column.getText())).setBold());
        }

        for (T row : tableView.getItems()) {
            for (TableColumn<T, ?> column : tableView.getColumns()) {
                Object cellData = column.getCellData(row);
                table.addCell(new Cell().add(new Paragraph(cellData != null ? cellData.toString() : "N/A")));
            }
        }

        return table;
    }

    /**
     * Ajoute un en-tête contenant un logo et un titre au document PDF.
     */
    private static void addHeader(Document document) throws IOException {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 5}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        String logoPath = "gestion-salles/src/ressources/logoIUT.jpg";
        Image logo = new Image(ImageDataFactory.create(logoPath)).setHeight(50);
        headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));

        Paragraph title = new Paragraph("SALTISTIQUE")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
        headerTable.addCell(new Cell().add(title).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

        document.add(headerTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute des informations générales au document PDF.
     *
     * @param document          Le document PDF où les informations seront ajoutées.
     * @param pdfName           Le nom du fichier PDF sans l'extension.
     * @param formattedFileDate La date formatée pour le fichier.
     * @param formattedDisplayDate La date formatée pour l'affichage dans le PDF.
     */
    private static void addDocumentInfo(Document document, String pdfName, String formattedFileDate, String formattedDisplayDate) {
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{2, 5}));
        infoTable.setWidth(UnitValue.createPercentValue(100));

        // Générer le nom du document à partir du nom du fichier et de la date
        String documentName = pdfName + formattedFileDate;

        infoTable.addCell(new Cell().add(new Paragraph("Nom du Document")).setBold());
        infoTable.addCell(new Cell().add(new Paragraph(documentName)));
        infoTable.addCell(new Cell().add(new Paragraph("Date")).setBold());
        infoTable.addCell(new Cell().add(new Paragraph(formattedDisplayDate)));

        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ouvre automatiquement un fichier PDF dans le système d'exploitation par défaut.
     */
    public static void openPdf(String filePath) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                Runtime.getRuntime().exec("cmd /c start " + filePath);
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + filePath);
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
