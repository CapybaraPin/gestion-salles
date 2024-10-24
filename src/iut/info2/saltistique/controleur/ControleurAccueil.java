/*
 * ControleurAccueil.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 *
 * @author Tom GUTIERREZ
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controleur de la vue d'Accueil du logiciel
 *
 * @author Hugo ROBLES
 */
public class ControleurAccueil {

    /**
     * Bouton d'accès à la vue d'importation depuis des fichiers
     */
    @FXML
    private Button btnImporterFichier;

    /**
     * Bouton d'accès à la vue d'importation depuis le réseau
     */
    @FXML
    private Button btnImporterReseau;

    /**
     * Bouton d'accès à la vue d'aide
     */
    @FXML
    private Button btnAide;

    /**
     * Bouton de fermeture de l'application
     */
    @FXML
    private Button navbarBtnClose;

    /**
     * Icône de fermeture de la fenêtre
     */
    @FXML
    private Pane icoMaximize;

    /**
     * Bouton de réduction de la fenêtre
     */
    @FXML
    private Button navbarBtnMinimize;

    /** Position de la souris en abscisse */
    double xOffset = 0;

    /** Position de la souris en ordonné */
    double yOffset = 0;

    /**
     * Initialise différents éléments de la vue d'accueil.
     */
    @FXML
    void initialize() {
        setHoverEffect();
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
     * Permet la gestion du click sur le bouton d'accès
     * à l'importantion depuis le réseau.
     */
    @FXML
    void clickImporterReseau() {
        Saltistique.showPopUp(Scenes.IMPORTATION_RESEAU);
    }

    /**
     * Ajoute les effets de survol sur les boutons de la barre de navigation.
     */
    private void setHoverEffect() {
        navbarBtnClose.setOnMouseEntered(event -> {
            icoMaximize.getStyleClass().add("bg-gray-light");
        });
        navbarBtnClose.setOnMouseExited(event -> {
            icoMaximize.getStyleClass().remove("bg-gray-light");
        });
    }

    /**
     * récupère les coordonnées de la souris lors du clique
     * @param event
     */
    @FXML
    void clicked(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    /**
     * Permet de déplacer la fenêtre en fonction des coordonnées de la souris
     * @param event
     */
    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(javafx.scene.input.TransferMode.ANY);
        }
    }

    /**
     * Lorsque des fichiers sont déposés dans la fenêtre, on les importe
     */
    @FXML
    void filesDragged(DragEvent event) throws IOException {
        List files = event.getDragboard().getFiles();
        String[] chemins;
        chemins = new String[files.size()];

        for (Object file : files) {
            chemins[files.indexOf(file)] = ((File) file).getAbsolutePath();
        }

        Saltistique.gestionDonnees.importerDonnees(chemins);
    }

    /**
     * Ouvre l'explorateur de fichier pour sélectionner les fichiers à importer
     */
    @FXML
    void clickImporterFichier(MouseEvent event) {
        System.out.println("test");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez les fichiers à importer");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        // on récupère la fenêtre principale pour afficher la fenêtre de sélection
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);

        if (files != null) {
            String[] chemins = new String[files.size()];
            for (int i = 0; i < files.size(); i++) {
                chemins[i] = files.get(i).getAbsolutePath();
            }
            Saltistique.gestionDonnees.importerDonnees(chemins);
        }
    }
}
