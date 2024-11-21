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
 *
 * @author Néo Bécogné
 */
public class GenerePDF {

    /**
     * Génère un fichier PDF à partir d'un {@link TableView} donné et l'ouvre automatiquement.
     *
     * @param tableView Le {@link TableView} contenant les données à exporter au format PDF.
     *                  Les colonnes du tableau définissent les en-têtes,
     *                  et les lignes fournissent les données du tableau.
     * @param <T>       Le type des éléments contenus dans les lignes du {@link TableView}.
     * @throws IOException              Si une erreur survient lors de l'écriture du fichier PDF.
     * @throws IllegalArgumentException Si le {@link TableView} est null ou vide.
     */
    public static <T> void generateAndOpenPdf(TableView<T> tableView) throws IOException {
        if (tableView == null || tableView.getItems().isEmpty()) {
            throw new IllegalArgumentException("Le tableau est vide ou null.");
        }
        String downloadsDir = System.getProperty("user.home") + File.separator + "Downloads";
        String dest = downloadsDir + File.separator + "Saltistique.pdf";

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        addHeader(document);

        addDocumentInfo(document);

        Table pdfTable = createTableFromTableView(tableView);
        document.add(pdfTable);

        document.close();

        openPdf(dest);
    }

    /**
     * Crée un tableau PDF à partir des données et des colonnes d'un {@link TableView}.
     *
     * @param tableView Le {@link TableView} contenant les données à convertir en tableau PDF.
     * @param <T>       Le type des éléments contenus dans le {@link TableView}.
     * @return Un tableau PDF ({@link Table}) construit à partir du contenu du {@link TableView}.
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
     *
     * @param document Le document PDF où l'en-tête sera ajouté.
     * @throws IOException Si une erreur survient lors du chargement de l'image du logo.
     */
    private static void addHeader(Document document) throws IOException {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 5}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        String logoPath = "C:/Users/neobe/OneDrive/Images/IUT/Bloc logo IUT 2024.jpg";
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
     * Ajoute des informations générales au document PDF (nom du document, date, etc.).
     *
     * @param document Le document PDF où les informations seront ajoutées.
     */
    private static void addDocumentInfo(Document document) {
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{2, 5}));
        infoTable.setWidth(UnitValue.createPercentValue(100));

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        infoTable.addCell(new Cell().add(new Paragraph("Nom du Document")).setBold());
        infoTable.addCell(new Cell().add(new Paragraph("R1320240687042732")));
        infoTable.addCell(new Cell().add(new Paragraph("Date")).setBold());
        infoTable.addCell(new Cell().add(new Paragraph(formattedDate)));

        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ouvre automatiquement un fichier PDF dans le système d'exploitation par défaut.
     *
     * @param filePath Le chemin du fichier PDF à ouvrir.
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
