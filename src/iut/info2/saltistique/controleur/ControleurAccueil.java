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
import javafx.scene.layout.VBox;
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
     * Bouton de fermeture de l'application
     */
    @FXML
    private Button navbarBtnClose;

    /**
     * Menu de navigation
     */
    @FXML
    private VBox menuContainer;

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

    /**
     * Position de la souris en abscisse
     */
    double xOffset = 0;

    /**
     * Position de la souris en ordonné
     */
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

    /**
     * Permet de gérer le drag and drop de fichiers
     * Accepte le transfert si des fichiers sont sur le point d'être déposés
     * @param event
     */
    @FXML
    void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(javafx.scene.input.TransferMode.ANY);
        }
    }

    /**
     * Lorsque des fichiers sont déposés dans la fenêtre, on les importe
     * @param event évenement de drag and drop
     */
    @FXML
    void filesDragged(DragEvent event) throws IOException {
        List files = event.getDragboard().getFiles();
        String[] chemins;
        chemins = new String[files.size()];

        for (Object file : files) {
            chemins[files.indexOf(file)] = ((File) file).getAbsolutePath();
        }

        try {
            Saltistique.gestionDonnees.importerDonnees(chemins);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'importation des fichiers : " + e.getMessage());
        }
    }

    /**
     * Ouvre l'explorateur de fichier pour sélectionner les fichiers à importer
     * @param event évenement de clique de souris
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
            try {
                Saltistique.gestionDonnees.importerDonnees(chemins);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'importation des fichiers : " + e.getMessage());
            }
        }
    }

    /**
     * Permet de gérer l'affichage du menu de navigation
     * lors du clique sur l'icône de burger
     */
    @FXML
    void burgerClicked() {
        if (menuContainer.isVisible()) {
            menuContainer.setVisible(false);
            menuContainer.setMouseTransparent(true);
        } else {
            menuContainer.setVisible(true);
            menuContainer.setMouseTransparent(false);
        }
    }

    /**
     * Permet de réduire la fenêtre
     * @param event
     */
    @FXML
    void reduireFenetre(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
