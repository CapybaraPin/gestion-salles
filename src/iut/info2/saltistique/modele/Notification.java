package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.ControleurAccueil;
import iut.info2.saltistique.controleur.ControleurConsulterDonnees;
import iut.info2.saltistique.controleur.ControleurDonneesIncorrectes;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// TODO : rendre la classe Notification générique pour éviter la duplication de code
// TODO : faire en sorte de séparer le titre et la description en fonction de l'exception
//        exemple : "Erreur de connexion : Impossible de se connecter au serveur."
//                : {"Erreur de connexion", "Impossible de se connecter au serveur."}
public class Notification {

    // Champs d'instance pour les informations de notification
    private String titre;
    private String description;

    ControleurAccueil controleurAccueil = Saltistique.getController(Scenes.ACCUEIL);
    ControleurConsulterDonnees controleurConsulterDonnees = Saltistique.getController(Scenes.CONSULTER_DONNEES);
    ControleurDonneesIncorrectes controleurDonneesIncorrectes = Saltistique.getController(Scenes.CONSULTER_DONNEES_INCORRECTES);

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
    private void afficher() {

        for(Scenes scene : Saltistique.scenes.keySet()) {
            if (Saltistique.getPrimaryStage().getScene() == Saltistique.scenes.get(scene)) {
                switch (scene) {
                    case ACCUEIL:
                        controleurAccueil.cadreNotification.setMouseTransparent(false);
                        controleurAccueil.titreNotification.setText(titre);
                        controleurAccueil.descriptionNotification.setText(description);
                        controleurAccueil.cadreNotification.setVisible(true);
                        break;
                    case CONSULTER_DONNEES:
                        controleurConsulterDonnees.cadreNotification.setMouseTransparent(false);
                        controleurConsulterDonnees.titreNotification.setText(titre);
                        controleurConsulterDonnees.descriptionNotification.setText(description);
                        controleurConsulterDonnees.cadreNotification.setVisible(true);
                        break;
                    case CONSULTER_DONNEES_INCORRECTES:
                        controleurDonneesIncorrectes.cadreNotification.setMouseTransparent(false);
                        controleurDonneesIncorrectes.titreNotification.setText(titre);
                        controleurDonneesIncorrectes.descriptionNotification.setText(description);
                        controleurDonneesIncorrectes.cadreNotification.setVisible(true);
                        break;
                }

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {

                    // Applique un effet de fondu de fermeture
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    if (scene == Scenes.ACCUEIL) {
                        fadeOut.setNode(controleurAccueil.cadreNotification);
                    } else if (scene == Scenes.CONSULTER_DONNEES) {
                        fadeOut.setNode(controleurConsulterDonnees.cadreNotification);
                    } else if (scene == Scenes.CONSULTER_DONNEES_INCORRECTES) {
                        fadeOut.setNode(controleurDonneesIncorrectes.cadreNotification);
                    }

                    fadeOut.setOnFinished(e -> {
                        if (scene == Scenes.ACCUEIL) {
                            controleurAccueil.cadreNotification.setVisible(false);
                            controleurAccueil.cadreNotification.setOpacity(1.0);
                        } else if (scene == Scenes.CONSULTER_DONNEES) {
                            controleurConsulterDonnees.cadreNotification.setVisible(false);
                            controleurConsulterDonnees.cadreNotification.setOpacity(1.0);
                        } else if (scene == Scenes.CONSULTER_DONNEES_INCORRECTES) {
                            controleurDonneesIncorrectes.cadreNotification.setVisible(false);
                            controleurDonneesIncorrectes.cadreNotification.setOpacity(1.0);
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
