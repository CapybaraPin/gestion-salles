package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.Controleur;
import iut.info2.saltistique.controleur.ControleurAccueil;
import iut.info2.saltistique.controleur.ControleurConsulterDonnees;
import iut.info2.saltistique.controleur.ControleurDonneesIncorrectes;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * <b>Notification</b> est une classe qui permet d'afficher des notifications à l'utilisateur.
 * <p>
 *     Les notifications sont affichées en haut de l'interface utilisateur et disparaissent automatiquement
 *     après 7 secondes.
 *     <br>
 *     Les notifications sont composées d'un titre et d'une description.
 *     <br>
 *     <br>
 * </p>
 */
public class Notification {

    /** Le titre de la notification */
    private String titre;

    /** La description de la notification */
    private String description;

    // Récupère le controleur actuel
    Controleur controleur = Saltistique.getController(Saltistique.getPrimaryStage().getScene());

    /**
     * Constructeur de Notification
     *
     * @param titre       Le titre de la notification
     * @param description La description de la notification
     */
    public Notification(String titre, String description) {
        this.titre = titre;
        this.description = description;

        afficher();
    }

    /**
     * Affiche la notification dans l'interface pendant 7 secondes avec un effet de fondu à la fermeture.
     */
    private void afficher() {


        controleur.cadreNotification.setMouseTransparent(false);
        controleur.titreNotification.setText(titre);
        controleur.descriptionNotification.setText(description);
        controleur.cadreNotification.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> {

            // Applique un effet de fondu de fermeture
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setNode(controleur.cadreNotification);

            fadeOut.setOnFinished(e -> {
                controleur.cadreNotification.setVisible(false);
                controleur.cadreNotification.setOpacity(1.0);
            });

            fadeOut.play();
        }));

        timeline.setCycleCount(1);
        timeline.play();
    }
}
