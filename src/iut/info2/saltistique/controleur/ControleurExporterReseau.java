/*
 * ControleurImporterReseau.java SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.donnees.ExportationReseau;
import iut.info2.saltistique.modele.donnees.Importation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Controleur de la vue d'importation des données depuis le réseau.
 *
 * @author Hugo ROBLES
 */
public class ControleurExporterReseau extends Controleur {

    /**
     * Champ contenant le port renseigné par l'utilisateur
     */
    @FXML
    private TextField port;

    /**
     * Label contenant l'adresse IP de la machine
     */
    @FXML
    private Label adresseIp;

    /**
     * Bouton d'éxecution de l'importation
     */
    @FXML
    private Button btnStartStop;

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
        Importation importerDonnees = new Importation(Saltistique.gestionDonnees);
        ExportationReseau exporterDonnees = new ExportationReseau(Integer.parseInt(port.getText()),
                                                                  importerDonnees.getFichiers());

        if (exporterDonnees.getServeur() != null) {

            // Arrêt du serveur
            exporterDonnees.getServeur().arreter();
            exporterDonnees.setServeur(null);
            btnStartStop.setText("Démarrer le serveur");
        } else {

            // Démarrage du serveur
            exporterDonnees.exportationDonnees();
            btnStartStop.setText("Arrêter le serveur");
        }
    }
}
