/*
 * ControleurAccueil.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 *
 * @autor Tom GUTIERREZ
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controleur de la vue d'Accueil du logiciel
 *
 * @author Hugo ROBLES
 */
public class ControleurAccueil {


    /** Fenêtre principale de l'application */
    @FXML
    private Stage primaryStage;

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


    /**
     * Permet la gestion du click sur le bouton d'accès
     * à l'importantion depuis le réseau.
     */
    @FXML
    void clickImporterReseau() {
        System.out.println("Importer réseau");

        Saltistique.showPopUp(Scenes.IMPORTATION_RESEAU);
    }

    @FXML
    void initialize() {
        setHoverEffect();
    }

    private void setHoverEffect() {
        navbarBtnClose.setOnMouseEntered(event -> {
            icoMaximize.getStyleClass().add("bg-gray-light");
        });
        navbarBtnClose.setOnMouseExited(event -> {
            icoMaximize.getStyleClass().remove("bg-gray-light");
        });
    }


}
