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
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;

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

    /**
     * Permet la gestion du click sur le bouton de démarage de l'importation
     */
    @FXML
    void clickImporter() {
        Saltistique.gestionDonnees.importerDonnees(adresseIp.getText(), Integer.parseInt(port.getText()));
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
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     * Permet de gérer le drag de la fenêtre (optionnel si vous voulez rendre la fenêtre mobile).
     * @param event évenement de clique de souris
     */
    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

}
