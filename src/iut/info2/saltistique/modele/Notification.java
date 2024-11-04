package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.ControleurAccueil;

public class Notification extends Exception {

    // Champs d'instance pour les informations de notification
    private String titre;
    private String description;

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
        ControleurAccueil controleurAccueil = Saltistique.getController(Scenes.ACCUEIL);

        controleurAccueil.notificationFrame.setMouseTransparent(false);

        // Définir le titre et la description de la notification
        controleurAccueil.notificationTitre.setText(titre);
        controleurAccueil.notificationDescription.setText(description);

        // Afficher le cadre de notification
        controleurAccueil.notificationFrame.setVisible(true);


    }
}
