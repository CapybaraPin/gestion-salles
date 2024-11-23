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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

    private InetAddress localIP;

    /**
     * Initialise différents éléments de la vue d'accueil.
     */
    @FXML
    void initialize() {
        try {
            Socket socket = new Socket("8.8.8.8", 443);
            localIP = socket.getLocalAddress();
            adresseIp.setText(localIP.getHostAddress());
            socket.close();
        } catch (UnknownHostException e) {
            adresseIp.setText("Erreur : IP non trouvée");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void arreterServeur() {
        if (exporterDonnees != null) {
            // Arrêt du serveur
            exporterDonnees.getServeur().arreter();
            exporterDonnees.setServeur(null);
            exporterDonnees = null;
            btnStartStop.setText("Démarrer le serveur");
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
            exporterDonnees = null;
            btnStartStop.setText("Démarrer le serveur");
        } else {
            try {
                int numeroPort = Integer.parseInt(port.getText());

                if (exporterDonnees == null) {
                    javafx.application.Platform.runLater(() -> {
                        exporterDonnees = new ExportationReseau(numeroPort, Saltistique.gestionDonnees.getFichiers(), localIP);
                        new Notification("Exportation des données", "Vous avez bien exporté les données sur le réseau.");
                    });
                }

                btnStartStop.setText("Arrêter le serveur");
            } catch (Exception e) {
                new Notification("Erreur lors de l'exportation", e.getMessage());
            }
        }
    }

    @FXML
    public void fermerFenetre() {
        if (exporterDonnees != null) {
            // Arrêt du serveur
            exporterDonnees.getServeur().arreter();
            exporterDonnees.setServeur(null);
            exporterDonnees = null;
            btnStartStop.setText("Démarrer le serveur");
        }
        super.fermerFenetre();
    }

}
