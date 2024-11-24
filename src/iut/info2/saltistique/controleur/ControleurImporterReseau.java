/*
 * ControleurImporterReseau.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.Scenes;
import iut.info2.saltistique.modele.donnees.Importation;
import iut.info2.saltistique.modele.donnees.ImportationReseau;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * Contrôleur pour la vue d'importation des données depuis le réseau.
 * Cette classe gère les interactions utilisateur et met à jour l'interface graphique
 * pour permettre le téléchargement et l'importation de données à partir d'une connexion réseau.
 *
 * @author Hugo ROBLES
 */
public class ControleurImporterReseau extends Controleur {

    /** Champ contenant l'adresse IP renseignée par l'utilisateur.  */
    @FXML
    private TextField adresseIp;

    /** Champ contenant le port renseigné par l'utilisateur.  */
    @FXML
    private TextField port;

    /** Étiquette affichant le texte lié à la barre de progression.  */
    @FXML
    private Label progressLabel;

    /** Conteneur graphique regroupant la barre de progression et le texte associé. */
    @FXML
    private VBox progressVbox;

    /** Barre de progression affichant l'avancement de l'importation. */
    @FXML
    private ProgressBar progressBar;

    /**
     * Initialise différents éléments de la vue.
     */
    @FXML
    void initialize() {
        setHoverEffect();
    }

    /**
     * Gestionnaire d'événement déclenché lors du clic sur le bouton d'importation.
     * Lance l'importation des données depuis le réseau en utilisant les informations fournies
     * (adresse IP et port).
     *
     * @throws Notification Affiche une notification en cas d'erreur lors de l'importation.
     */
    @FXML
    void clickImporter() {
        try {
            ImportationReseau importationReseau = new ImportationReseau(
                    adresseIp.getText(),
                    Integer.parseInt(port.getText()),
                    Saltistique.gestionDonnees
            );
        } catch (Exception e) {
            e.printStackTrace(); // Affiche les détails dans la console pour le débogage.
            new Notification("Erreur lors de l'importation des fichiers", e.getMessage());
        }
    }

    /**
     * Appelée lorsque l'importation des données réseau est terminée.
     * Charge les données dans le gestionnaire de données, met à jour la vue associée et
     * notifie l'utilisateur du succès ou de l'échec de l'opération.
     *
     * @param dossier Chemin du dossier contenant les fichiers téléchargés.
     */
    @FXML
    public void finInmportationReseau(String dossier) {
        javafx.application.Platform.runLater(() -> {
            fermerFenetre();

            ControleurConsulterDonnees controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES);

            File dossierSauvegarde = new File(dossier);
            File[] fichiersExistants = dossierSauvegarde.listFiles();

            if (fichiersExistants != null && fichiersExistants.length > 0) {
                String[] cheminFichiers = new String[4];
                for (int i = 0; i < 4; i++) {
                    cheminFichiers[i] = fichiersExistants[i].getAbsolutePath();
                }
                try {
                    Importation importationDonnees = new Importation(Saltistique.gestionDonnees);
                    importationDonnees.importerDonnees(cheminFichiers);
                } catch (Exception e) {
                    new Notification("Erreur lors de l'importation des fichiers : ", e.getMessage());
                    return;
                }
                controleur.rafraichirTableaux();
                Saltistique.changeScene(Scenes.CONSULTER_DONNEES);

                new Notification("Importation réussie", "Les données ont été importées avec succès.");
            } else {
                new Notification("Erreur d'importation", "Erreur lors de la réception des fichiers.");
            }

            new Notification("Importation réussie", "Les données ont été importées avec succès.");
        });
    }

    /**
     * Met à jour l'affichage de la barre de progression en fonction de l'avancement.
     * Cette méthode est appelée sur le thread de l'interface graphique JavaFX pour
     * assurer une mise à jour fluide et réactive.
     *
     * @param progress Pourcentage d'avancement de l'importation (entre 0 et 1).
     */
    public void miseAJourProgression(double progress) {
        javafx.application.Platform.runLater(() -> {
            if (!progressVbox.isVisible()) {
                progressVbox.setVisible(true);
                progressBar.setProgress(0);
            }
            progressBar.setProgress(progress);
            progressLabel.setText("Progression : " + (int) (progress * 100) + "%");
        });
    }
}
