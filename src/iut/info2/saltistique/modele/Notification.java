package iut.info2.saltistique.modele;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Notification {

    // Éléments d'interface partagés pour afficher une notification
    private static HBox notificationFrame;
    private static Label notificationTitre;
    private static Label notificationDescription;
    private static Button notificationBtn;

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

        System.out.println(this.titre + " " + this.description);
    }

    /**
     * Initialiser les éléments d'interface pour afficher les notifications.
     * Appelée depuis le contrôleur.
     *
     * @param frame Le conteneur principal de la notification
     * @param titre Le label pour le titre de la notification
     * @param description Le label pour la description de la notification
     * @param btn Le bouton de la notification
     */
    public void setNotificationElements(HBox frame, Label titre, Label description, Button btn) {
        notificationFrame = frame;
        notificationTitre = titre;
        notificationDescription = description;
        notificationBtn = btn;
    }

    /**
     * Affiche la notification dans l'interface.
     */
    public void afficher() {
        if (notificationFrame != null && notificationTitre != null && notificationDescription != null) {
            // Définir le titre et la description de la notification
            notificationTitre.setText(titre);
            notificationDescription.setText(description);

            // Afficher le cadre de notification
            notificationFrame.setVisible(true);

            // Configurer le bouton de fermeture
            if (notificationBtn != null) {
                notificationBtn.setOnAction(e -> notificationFrame.setVisible(false));
                notificationBtn.setVisible(true);
            }
        } else {
            System.err.println("Les éléments de l'interface pour la notification ne sont pas configurés.");
        }
    }
}
