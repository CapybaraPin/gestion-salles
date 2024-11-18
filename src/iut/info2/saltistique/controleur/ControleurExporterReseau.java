/*
 * ControleurImporterReseau.java SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.donnees.ExportationReseau;
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
     * Exportation des données via le réseau
     */
    private ExportationReseau exporterDonnees;

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
        if (exporterDonnees != null) {
            // Arrêt du serveur
            exporterDonnees.getServeur().arreter();
            exporterDonnees.setServeur(null);
            btnStartStop.setText("Démarrer le serveur");
        } else {
            try {
                int numeroPort = Integer.parseInt(port.getText());

                if (exporterDonnees == null) {
                    javafx.application.Platform.runLater(() -> {
                        exporterDonnees = new ExportationReseau(numeroPort, Saltistique.gestionDonnees.getFichiers());
                        new Notification("Exportation des données", "Vous avez bien exporté les données sur le réseau.");
                    });
                }

                btnStartStop.setText("Arrêter le serveur");
            } catch (Exception e) {
                new Notification("Erreur lors de l'exportation", e.getMessage());
            }
        }
    }

}
