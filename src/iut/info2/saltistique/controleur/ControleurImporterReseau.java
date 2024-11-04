/*
 * ControleurImporterReseau.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controleur de la vue d'importation des données depuis le réseau.
 *
 * @author Hugo ROBLES
 */
public class ControleurImporterReseau {


    double x,y;

    /** Champ contenant l'adresse IP renseignée par l'utilisateur */
    @FXML
    private TextField adresseIp;

    /** Champ contenant le port renseigné par l'utilisateur */
    @FXML
    private TextField port;

    /** Bouton d'éxecution de l'importation */
    @FXML
    private Button btnImporter;

    /** Bouton d'appel de la notice */
    @FXML
    private Button btnAide;

    /** Position de la souris en abscisse */
    double xOffset = 0;

    /** Position de la souris en ordonné */
    double yOffset = 0;

    @FXML
    private Label progressLabel;

    @FXML
    private VBox progressVbox;

    @FXML
    private ProgressBar progressBar;

    /**
     * Permet la gestion du click sur le bouton de démarage de l'importation
     */
    @FXML
    void clickImporter() {
        try {
            Saltistique.gestionDonnees.importerDonnees(adresseIp.getText(), Integer.parseInt(port.getText()));
        } catch (Exception e) {
            return;
        }
        progressControl(0.4f);

        ControleurConsulterDonnees controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES);
        controleur.rafraichirTableaux();
        Saltistique.changeScene(Scenes.CONSULTER_DONNEES);

        Stage stage = (Stage) adresseIp.getScene().getWindow();
        stage.close();


    }

    /**
     * Si la valeur de paramètre est à null alors la vbox est pas visible,
     * sinon la valeur est définie sur la progressBar
     */
    @FXML
    void progressControl(float valeur) {
        progressVbox.setVisible(true);
        progressBar.setProgress(valeur);
        progressLabel.setText("Importation des données en cours : " + valeur + "%");

    }

    public void onProgressUpdate(double progress) {
        // Met à jour l'interface graphique dans le thread JavaFX
        javafx.application.Platform.runLater(() -> {
            progressBar.setProgress(progress);
            progressLabel.setText("Progression : " + (int) (progress * 100) + "%");
        });
    }

    /**
     * Ferme la fenêtre actuelle.
     * @param event évenement de clique de souris
     */
    @FXML
    void fermerFenetre(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Permet de vérifier les coordonnées de clique d'une souris
     * @param event évenement de clique de souris
     */
    @FXML
    void clicked(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    /**
     * Permet de gérer le drag de la fenêtre (optionnel si vous voulez rendre la fenêtre mobile).
     * @param event évenement de clique de souris
     */
    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}
