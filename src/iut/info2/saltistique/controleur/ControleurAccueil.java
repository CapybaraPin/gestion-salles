/*
 * ControleurAccueil.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Controleur de la vue d'Accueil du logiciel
 *
 * @author Tom GUTIERREZ & Hugo ROBLES
 */
public class ControleurAccueil extends Controleur {


    /**
     * Titre de la notification
     */
    @FXML
    public Text titreNotification;

    /**
     * Description de la notification
     */
    @FXML
    public Text descriptionNotification;

    /**
     * Initialise différents éléments de la vue d'accueil.
     */
    @FXML
    void initialize() {
        setHoverEffect();
        clickBoutonNotification();
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
     * Gère l'événement de drag and drop de fichiers lorsque l'utilisateur fait glisser des fichiers
     * au-dessus de la zone cible. Cette méthode vérifie si le contenu en cours de drag and drop
     * contient des fichiers et, si c'est le cas, accepte le transfert.
     *
     * @param event l'événement de drag and drop contenant les informations sur les fichiers glissés
     */
    @FXML
    void lorsqueGlisser(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(javafx.scene.input.TransferMode.ANY);
        }
    }

    /**
     * Lorsque des fichiers sont déposés dans la fenêtre, on les importe
     *
     * @param event évenement de drag and drop
     */
    @FXML
    void lorsqueFichiersGlisses(DragEvent event) {
        List<File> files = event.getDragboard().getFiles(); // TODO : trouver un autre moyen que List
        String[] chemins;
        chemins = new String[files.size()];
        for (Object file : files) {
            chemins[files.indexOf(file)] = ((File) file).getAbsolutePath();
        }
        importerDonnees(chemins);
    }

    /**
     * Ouvre l'explorateur de fichier pour sélectionner les fichiers à importer
     *
     * @param event évenement de clique de souris
     */
    @FXML
    void clickImporterFichier(MouseEvent event) {
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
            importerDonnees(chemins);
        }
    }

    /**
     * Permet d'importer les données depuis les fichiers spécifiés
     * Si une erreur survient lors de l'importation, une notification est affichée
     * Si l'importation est réussie, la vue de consultation des données est affichée
     * Si l'importation est réussie mais que des lignes ont été ignorées, la vue
     * de consultation des données ignorée est affichée
     *
     * @param chemins les chemins des fichiers à importer
     */
    private void importerDonnees(String[] chemins) {
        ArrayList<String[]> lignesIncorrectesSalles;
        ArrayList<String[]> lignesIncorrectesActivites;
        ArrayList<String[]> lignesIncorrectesReservations;
        ArrayList<String[]> lignesIncorrectesUtilisateurs;

        try {
            Saltistique.gestionDonnees.importerDonnees(chemins);
        } catch (Exception e) {
            new Notification("Erreur lors de l'importation des fichiers : ", e.getMessage());
            return;
        }

        lignesIncorrectesSalles = Saltistique.gestionDonnees.getLignesIncorrectesSalles();
        lignesIncorrectesActivites = Saltistique.gestionDonnees.getLignesIncorrectesActivites();
        lignesIncorrectesReservations = Saltistique.gestionDonnees.getLignesIncorrectesReservations();
        lignesIncorrectesUtilisateurs = Saltistique.gestionDonnees.getLignesIncorrectesUtilisateurs();

        if (!lignesIncorrectesSalles.isEmpty() || !lignesIncorrectesActivites.isEmpty()
                || !lignesIncorrectesReservations.isEmpty() || !lignesIncorrectesUtilisateurs.isEmpty()) {
            Saltistique.changeScene(Scenes.CONSULTER_DONNEES_INCORRECTES);
            ControleurDonneesIncorrectes controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES_INCORRECTES);
            controleur.rafraichirTableaux();

            new Notification("Importation réussie avec des lignes incorrectes", "Certaines lignes " +
                    "n'ont pas été importées correctement. Consultez les données incorrectes pour plus d'informations.");
        } else {
            Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
            ControleurConsulterDonnees controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES);
            controleur.rafraichirTableaux();

            new Notification("Importation réussie", "Les données ont été importées avec succès.");
        }
    }
}