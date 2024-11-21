package iut.info2.test.saltistique;

import com.itextpdf.kernel.pdf.PdfDocument;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import iut.info2.saltistique.modele.GenerePDF;

public class TestGenerePDF extends ApplicationTest {

    @Override
    public void start(javafx.stage.Stage stage) {
        // Setup nécessaire pour les tests JavaFX
    }

    @Test
    void testGeneratePdfValidTable() throws InterruptedException, IOException {
        // Tester la génération du PDF avec une TableView valide

        // Exécuter le code dans le thread JavaFX
        Platform.runLater(() -> {
            try {
                // Créer un TableView valide
                TableView<String> tableView = new TableView<>();
                TableColumn<String, String> column = new TableColumn<>("Column");
                tableView.getColumns().add(column);
                tableView.getItems().addAll("Item 1", "Item 2", "Item 3");

                // Appeler la méthode de génération de PDF
                GenerePDF.generateAndOpenPdf(tableView, "TestFile");

                // Vérifier que le fichier a bien été généré
                String downloadsDir = System.getProperty("user.home") + File.separator + "Downloads";
                String fileName = "TestFile" + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf";
                File pdfFile = new File(downloadsDir + File.separator + fileName);

                assertTrue(pdfFile.exists(), "Le fichier PDF a été généré");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la génération du PDF", e);
            }
        });

        // Attendre que Platform.runLater termine l'exécution avant de terminer le test
        Thread.sleep(2000);
    }

    @Test
    void testGeneratePdfEmptyTable() throws InterruptedException {
        // Tester la génération du PDF avec une TableView vide

        Platform.runLater(() -> {
            try {
                TableView<String> tableView = new TableView<>();
                tableView.getItems().add("Test Item 1");

                // Exécuter la génération de PDF
                GenerePDF.generateAndOpenPdf(tableView, "");

                // Vérifier si le PDF a bien été généré
                String downloadsDir = System.getProperty("user.home") + "/Downloads";
                String fileName = "TestFile" + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf";
                File pdfFile = new File(downloadsDir, fileName);

                assertTrue(pdfFile.exists(), "Le fichier PDF a été généré");

            } catch (Exception e) {
                e.printStackTrace();
                fail("Exception dans le test: " + e.getMessage());
            }
        });

        // Attendre que Platform.runLater termine son exécution
        Thread.sleep(2000);
    }

    @Test
    void testGeneratePdfEmptyFileName() {
        // Tester l'exception lancée pour un nom de fichier vide

        TableView<String> tableView = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Column");
        tableView.getColumns().add(column);
        tableView.getItems().addAll("Item 1");

        // Tester que l'exception est levée pour un nom de fichier vide
        assertThrows(IllegalArgumentException.class, () -> {
            GenerePDF.generateAndOpenPdf(tableView, "");
        }, "Le nom du fichier PDF ne peut pas être vide.");
    }

    @Test
    void testHeaderAndDocumentInfo() throws IOException, InterruptedException {
        // Tester la génération du PDF et la présence d'informations dans le document PDF

        Platform.runLater(() -> {
            try {
                // Créer un TableView valide
                TableView<String> tableView = new TableView<>();
                TableColumn<String, String> column = new TableColumn<>("Column");
                tableView.getColumns().add(column);
                tableView.getItems().addAll("Item 1");

                // Appeler la méthode de génération de PDF
                GenerePDF.generateAndOpenPdf(tableView, "TestFile");

                // Vérifier que le fichier a bien été généré
                String downloadsDir = System.getProperty("user.home") + File.separator + "Downloads";
                String fileName = "TestFile" + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf";
                File pdfFile = new File(downloadsDir + File.separator + fileName);

                assertTrue(pdfFile.exists(), "Le fichier PDF a été généré");

                // Ouvrir le PDF et vérifier visuellement (ce test nécessite l'examen manuel ou un test plus approfondi du PDF)
                // Par exemple, en analysant le contenu du PDF avec une bibliothèque spécifique à PDF.
            } catch (IOException e) {
                e.printStackTrace();
                fail("Erreur lors de la génération du PDF: " + e.getMessage());
            }
        });

        // Attendre la fin du processus
        Thread.sleep(2000);
    }
}
