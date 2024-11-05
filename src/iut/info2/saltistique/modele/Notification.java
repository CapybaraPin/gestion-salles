package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.ControleurAccueil;
import iut.info2.saltistique.controleur.ControleurConsulterDonnees;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Notification extends Exception {

    // Champs d'instance pour les informations de notification
    private String titre;
    private String description;

    ControleurAccueil controleurAccueil = Saltistique.getController(Scenes.ACCUEIL);
    ControleurConsulterDonnees controleurConsulterDonnees = Saltistique.getController(Scenes.CONSULTER_DONNEES);

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
     * Affiche la notification dans l'interface pendant 8 secondes avec un effet de fondu à la fermeture.
     */
    public void afficher() {

        for(Scenes scene : Saltistique.scenes.keySet()) {
            if (Saltistique.getPrimaryStage().getScene() == Saltistique.scenes.get(scene)) {
                switch (scene) {
                    case ACCUEIL:
                        controleurAccueil.notificationFrame.setMouseTransparent(false);
                        controleurAccueil.notificationTitre.setText(titre);
                        controleurAccueil.notificationDescription.setText(description);
                        controleurAccueil.notificationFrame.setVisible(true);
                        break;
                    case CONSULTER_DONNEES:
                        controleurConsulterDonnees.notificationFrame.setMouseTransparent(false);
                        controleurConsulterDonnees.notificationTitre.setText(titre);
                        controleurConsulterDonnees.notificationDescription.setText(description);
                        controleurConsulterDonnees.notificationFrame.setVisible(true);
                        break;
                }

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {

                    // Applique un effet de fondu de fermeture
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    if (scene == Scenes.ACCUEIL) {
                        fadeOut.setNode(controleurAccueil.notificationFrame);
                    } else if (scene == Scenes.CONSULTER_DONNEES) {
                        fadeOut.setNode(controleurConsulterDonnees.notificationFrame);
                    }

                    fadeOut.setOnFinished(e -> {
                        if (scene == Scenes.ACCUEIL) {
                            controleurAccueil.notificationFrame.setVisible(false);
                            controleurAccueil.notificationFrame.setOpacity(1.0);
                        } else if (scene == Scenes.CONSULTER_DONNEES) {
                            controleurConsulterDonnees.notificationFrame.setVisible(false);
                            controleurConsulterDonnees.notificationFrame.setOpacity(1.0);
                        }
                    });

                    fadeOut.play();
                }));

                timeline.setCycleCount(1);
                timeline.play();
            }
        }
    }
}
