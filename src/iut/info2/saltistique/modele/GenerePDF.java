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

public class GenerePDF {

    public static <T> void generateAndOpenPdf(TableView<T> tableView) throws IOException {
        if (tableView == null || tableView.getItems().isEmpty()) {
            throw new IllegalArgumentException("Le tableau est vide ou null.");
        }

        String downloadsDir = System.getProperty("user.home") + File.separator + "Downloads";
        String dest = downloadsDir + File.separator + "Saltistique.pdf";

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        // Orientation horizontale
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        // En-tête avec logo et titre
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

        // Informations supplémentaires
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{2, 5}));
        infoTable.setWidth(UnitValue.createPercentValue(100));

        infoTable.addCell(new Cell().add(new Paragraph("Nom du Document")).setBold());
        infoTable.addCell(new Cell().add(new Paragraph("R1320240687042732")));
        infoTable.addCell(new Cell().add(new Paragraph("Date")).setBold());
        infoTable.addCell(new Cell().add(new Paragraph(formattedDate)));

        document.add(infoTable);
        document.add(new Paragraph("\n"));

        // Tableau principal généré à partir du TableView
        Table pdfTable = createTableFromTableView(tableView);
        document.add(pdfTable);


        document.close();
        openPdf(dest);
    }

    public static <T> Table createTableFromTableView(TableView<T> tableView) {
        // Récupérer les colonnes
        int numColumns = tableView.getColumns().size();
        Table table = new Table(UnitValue.createPercentArray(numColumns));
        table.setWidth(UnitValue.createPercentValue(100));

        // Ajouter les en-têtes
        for (TableColumn<T, ?> column : tableView.getColumns()) {
            table.addCell(new Cell().add(new Paragraph(column.getText())).setBold());
        }

        // Ajouter les données
        for (T row : tableView.getItems()) {
            for (TableColumn<T, ?> column : tableView.getColumns()) {
                // Obtenir la valeur de la cellule à partir de la colonne et de la ligne
                Object cellData = column.getCellData(row);
                table.addCell(new Cell().add(new Paragraph(cellData != null ? cellData.toString() : "N/A")));
            }
        }

        return table;
    }

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
