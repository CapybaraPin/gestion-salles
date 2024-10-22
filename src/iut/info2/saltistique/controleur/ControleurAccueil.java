/*
 * ControleurAccueil.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controleur de la vue d'Accueil du logiciel
 *
 * @author Hugo ROBLES
 */
public class ControleurAccueil {

    /** Bouton d'accès à la vue d'importation depuis des fichiers */
    @FXML
    private Button btnImporterFichier;

    /** Bouton d'accès à la vue d'importation depuis le réseau */
    @FXML
    private Button btnImporterReseau;

    /** Bouton d'accès à la vue d'aide */
    @FXML
    private Button btnAide;

    /**
     * Permet la gestion du click sur le bouton d'accès
     * à l'importantion depuis le réseau.
     */
    @FXML
    void clickImporterReseau() {
        System.out.println("Importer réseau");

        Saltistique.changeScene(Scenes.IMPORTATION_RESEAU);
    }

}
