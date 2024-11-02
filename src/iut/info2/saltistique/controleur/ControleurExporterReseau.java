/*
 * ControleurImporterReseau.java SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Controleur de la vue d'importation des données depuis le réseau.
 *
 * @author Hugo ROBLES
 */
public class ControleurExporterReseau {


    double x,y;

    /** Champ contenant le port renseigné par l'utilisateur */
    @FXML
    private TextField port;

    /** Label contenant l'adresse IP de la machine */
    @FXML
    private Label adresseIp;

    /** Bouton d'éxecution de l'importation */
    @FXML
    private Button btnStartStop;

    /** Position de la souris en abscisse */
    double xOffset = 0;

    /** Position de la souris en ordonné */
    double yOffset = 0;

    /**
     * Initialise différents éléments de la vue d'accueil.
     */
    @FXML
    void initialize() {
        try {
            String localIP = InetAddress.getLocalHost().getHostAddress();
            adresseIp.setText(localIP);
        } catch (UnknownHostException e) {
            adresseIp.setText("Erreur : IP non trouvée");
            e.printStackTrace();
        }
    }

    /**
     * Permet la gestion du click sur le bouton de démarage de l'importation
     */
    @FXML
    void clickStartStop() {
        if(Saltistique.gestionDonnees.serveur != null) {

            // Arrêt du serveur
            Saltistique.gestionDonnees.serveur.arreter();
            Saltistique.gestionDonnees.serveur = null;
            btnStartStop.setText("Démarrer le serveur");
        } else {

            // Démarrage du serveur
            Saltistique.gestionDonnees.exporterDonnees(Integer.parseInt(port.getText()));
            btnStartStop.setText("Arrêter le serveur");
        }
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
