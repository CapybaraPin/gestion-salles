/*
 * ControleurImporterReseau.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.Scenes;
import iut.info2.saltistique.modele.donnees.ImportationReseau;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controleur de la vue d'importation des données depuis le réseau.
 *
 * @author Hugo ROBLES
 */
public class ControleurImporterReseau extends Controleur {

    /**
     * Champ contenant l'adresse IP renseignée par l'utilisateur
     */
    @FXML
    private TextField adresseIp;

    /**
     * Champ contenant le port renseigné par l'utilisateur
     */
    @FXML
    private TextField port;

    /**
     * Texte correspondant à la bar de progression
     */
    @FXML
    private Label progressLabel;

    /**
     * Conteneur de la bar de progression et du texte
     */
    @FXML
    private VBox progressVbox;

    /**
     * Bar de progression
     */
    @FXML
    private ProgressBar progressBar;

    /**
     * Permet la gestion du click sur le bouton de démarrage de l'importation
     */
    @FXML
    void clickImporter() {
        try {
            ImportationReseau importationReseau = new ImportationReseau(
                    adresseIp.getText(),
                    Integer.parseInt(port.getText()),
                    Saltistique.gestionDonnees
            );
        } catch (Exception e) {
            e.printStackTrace(); // Affichez les détails dans la console
            new Notification("Erreur lors de l'importation des fichiers", e.getMessage());
        }
    }

    @FXML
    public void finInmportationReseau() {
        javafx.application.Platform.runLater(() -> {
            fermerFenetre();

            ControleurConsulterDonnees controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES);
            controleur.rafraichirTableaux();
            Saltistique.changeScene(Scenes.CONSULTER_DONNEES);

            new Notification("Importation réussie", "Les données ont été importées avec succès.");
        });
    }


    /**
     * Met à jour la bar de progression
     *
     * @param progress, correspond
     */
    public void miseAJourProgression(double progress) {
        // Met à jour l'interface graphique dans le thread JavaFX
        javafx.application.Platform.runLater(() -> {
            if (!progressVbox.isVisible()) {
                progressVbox.setVisible(true);
                progressBar.setProgress(0);
            }
            progressBar.setProgress(progress);
            progressLabel.setText("Progression : " + (int) (progress * 100) + "%");
        });
    }
}
