/*
 * ControleurImporterReseau.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
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
     * Permet la gestion du click sur le bouton de démarage de l'importation
     */
    @FXML
    void clickImporter() {
        Saltistique.gestionDonnees.importerDonnees(adresseIp.getText(), Integer.parseInt(port.getText()));
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
