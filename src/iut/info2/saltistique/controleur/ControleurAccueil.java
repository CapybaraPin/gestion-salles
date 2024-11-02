/*
 * ControleurAccueil.java               20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 *
 * @author Tom GUTIERREZ & Hugo ROBLES
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.awt.*;

/**
 * Controleur de la vue d'Accueil du logiciel
 *
 * @author Tom GUTIERREZ & Hugo ROBLES
 */
public class ControleurAccueil {
    /** Bouton de fermeture de l'application */
    @FXML
    private Button navbarBtnClose;

    /** Menu de navigation */
    @FXML
    private VBox menuContainer;

    /** layer permettant de fermer le menu de navigation
     * lors du clique en dehors de celui-ci */
    @FXML
    private Pane layerMenu;

    /** Icône de fermeture de la fenêtre */
    @FXML
    private Pane icoMaximize;

    /** Position de la souris en abscisse */
    double xOffset = 0;

    /** Position de la souris en ordonné */
    double yOffset = 0;

    /** Initialise différents éléments de la vue d'accueil. */
    @FXML
    void initialize() {
        setHoverEffect();
    }

    /**
     * Affchage d'une notification
     */
    static void afficherNotification(String titre, String description){

       // TODO : Comment faire ?
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
     * à l'importation depuis le réseau.
     */
    @FXML
    void clickImporterReseau() {
        Saltistique.showPopUp(Scenes.IMPORTATION_RESEAU);
    }

    /**
     * Permet la gestion du click sur le bouton de déchangement des données
     */
    @FXML
    void clickDechargerDonnees() {
        Saltistique.gestionDonnees.viderDonnees();
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
            ControleurConsulterDonnees controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES);
            controleur.rafraichirTableaux();
            Saltistique.changeScene(Scenes.CONSULTER_DONNEES);


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
                Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'importation des fichiers : " + e.getMessage());
            }
        }
    }

    /**
     * Permet de gérer l'affichage du menu de navigation
     * lorsque l'utilisateur clique sur l'icône de burger
     * celui ci s'affiche ou se cache
     */
    @FXML
    void burgerClicked() {
        if (menuContainer.isVisible()) {
            menuContainer.setVisible(false);
            menuContainer.setMouseTransparent(true);
            layerMenu.setMouseTransparent(true);
        } else {
            menuContainer.setVisible(true);
            menuContainer.setMouseTransparent(false);
            layerMenu.setMouseTransparent(false);
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


    /**
     * Handler pour le clic sur le bouton d'aide
     * Cela permet d'ouvrir le fichier PDF d'aide utilisateur
     * et de fermer le menu de navigation si celui-ci est ouvert
     */
    @FXML
    void handlerAide() {
        clickAide();
        if (menuContainer.isVisible()) {
            burgerClicked();
        }
    }

    /**
     * Handler pour le clic sur le bouton d'exportation des données
     * Cela permet d'ouvir la fenètre permettant l'exportation des données.
     */
    @FXML
    void handlerPartager() {
        Saltistique.showPopUp(Scenes.EXPORTER_RESEAU);
    }


    /**
     * Gère le clic sur le bouton d'aide et tente d'ouvrir un fichier PDF d'aide utilisateur.
     * Cette méthode vérifie si le fichier PDF spécifié existe, si le système prend en charge
     * l'ouverture de fichiers via Desktop, et tente d'ouvrir le fichier
     * Si l'une de ces vérifications échoue, une exception appropriée est levée et gérée.
     * @throws IOException si le fichier PDF n'existe pas ou s'il y a une erreur lors de l'ouverture du fichier.
     * @throws UnsupportedOperationException si Desktop n'est pas supportée sur le système ou si l'action d'ouverture
     * de fichier n'est pas supportée.
     */
    @FXML
    void clickAide() {
        try {
            File pdfFile = new File("src/ressources/noticeUtilisation.pdf");

            if (!pdfFile.exists()) {
                throw new IOException("Le fichier spécifié n'existe pas.");
            }

            if (!Desktop.isDesktopSupported()) {
                throw new UnsupportedOperationException("Desktop n'est pas supporté sur ce système.");
            }

            Desktop desktop = Desktop.getDesktop();

            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                throw new UnsupportedOperationException("L'ouverture de fichiers n'est pas supportée sur ce système.");
            }

            desktop.open(pdfFile);  // Ouvrir le fichier PDF

        } catch (IOException | UnsupportedOperationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

}
