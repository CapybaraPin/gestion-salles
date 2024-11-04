package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.ControleurAccueil;
import iut.info2.saltistique.controleur.ControleurConsulterDonnees;

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
     * Affiche la notification dans l'interface.
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
            }
        }

    }
}
