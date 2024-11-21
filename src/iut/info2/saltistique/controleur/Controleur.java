/*
 * Controleur.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Contrôleur général, permet d'importer toutes
 * les méthodes généralistes qui pourraient se répéter dans les autres contrôleurs.
 *
 * @author Jules VIALAS, Dorian ADAMS
 */
public class Controleur extends ControleurFiltres{

    /** Cadre de la notification */
    @FXML
    public VBox cadreNotification;

    /** Bouton de confirmation de la notification */
    @FXML
    public Button boutonNotification;

    /** Texte du titre de la notification */
    @FXML
    public Text titreNotification;

    /** Texte de la description de la notification */
    @FXML
    public Text descriptionNotification;

    /** Position de la souris en abscisse */
    double xOffset = 0;

    /** Position de la souris en ordonné */
    double yOffset = 0;

    /** Bouton de fermeture de la fenêtre */
    @FXML
    private Button btnFermer;

    /** Conteneur pour le menu de navigation. */
    @FXML
    private VBox conteneurMenu;

    /** Couche utilisée pour fermer le menu de navigation. */
    @FXML
    private Pane layerMenu;

    /** Icône de fermeture de la fenêtre */
    @FXML
    private Pane iconeFermetureFenetre;

    /**
     * Méthode permettant la fermeture de la fenêtre de l'application en cours d'exécution
     * Cette méthode est également appelée lors d'un évènement de clic
     */
    @FXML
    public void fermerFenetre() {
        Stage stage = (Stage) btnFermer.getScene().getWindow();
        stage.close();
    }

    /**
     * Réduit la fenêtre de l'application en cours d'exécution.
     * Cette méthode est appelée lors d'un événement de clic de souris pour minimiser la fenêtre.
     *
     * @param event l'événement de souris qui déclenche la réduction de la fenêtre
     */
    @FXML
    void reduireFenetre(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Permet de vérifier les coordonnées de clique d'une souris
     *
     * @param event événement de clique de souris
     */
    @FXML
    void clicked(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    /**
     * Permet de gérer le drag de la fenêtre (optionnel si vous voulez rendre la fenêtre mobile).
     *
     * @param event événement de clique de souris
     */
    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    /**
     * Permet de gérer l'affichage du menu de navigation
     * lorsque l'utilisateur clique sur l'icône de burger
     * celui-ci s'affiche ou se cache
     */
    @FXML
    void menuClick() {
        if (conteneurMenu.isVisible()) {
            conteneurMenu.setVisible(false);
            conteneurMenu.setMouseTransparent(true);
            layerMenu.setMouseTransparent(true);
        } else {
            conteneurMenu.setVisible(true);
            conteneurMenu.setMouseTransparent(false);
            layerMenu.setMouseTransparent(false);
        }
    }

    /**
     * Handler pour le clic sur le bouton d'aide
     * Cela permet d'ouvrir le fichier PDF d'aide utilisateur
     * et de fermer le menu de navigation si celui-ci est ouvert
     */
    @FXML
    void handlerAide() {
        clickAide();
        if (conteneurMenu.isVisible()) {
            menuClick();
        }
    }

    /**
     * Gère le clic sur le bouton d'aide et tente d'ouvrir un fichier PDF d'aide utilisateur.
     * Cette méthode vérifie si le fichier PDF spécifié existe, si le système prend en charge
     * l'ouverture de fichiers via Desktop, et tente d'ouvrir le fichier
     * Si l'une de ces vérifications échoue, une exception appropriée est levée et gérée.
     *
     * @throws UnsupportedOperationException si Desktop n'est pas supportée sur le système ou si l'action d'ouverture
     *                                       de fichier n'est pas supportée.
     */
    public void clickAide() {
        try {
            File pdfFile = new File("src/ressources/noticeUtilisation.pdf");
            if (!pdfFile.exists()) {
                throw new IOException("Le fichier spécifié n'existe pas.");
            }

            if (!Desktop.isDesktopSupported()) {
                throw new UnsupportedOperationException("Desktop n'est pas supporté sur ce système.");
            }

            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                throw new UnsupportedOperationException("L'ouverture de fichiers n'est pas supportée sur ce système.");
            }

            desktop.open(pdfFile);

        } catch (IOException | UnsupportedOperationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    /**
     * Gère le clic sur le bouton de chargement des données.
     * Vide les données du modèle et change de scène pour l'accueil.
     * Affiche une notification pour informer l'utilisateur du succès de l'opération.
     */
    @FXML
    void clickDechargerDonnees() {
        if (conteneurMenu.isVisible()) {
            menuClick();
        }

        Saltistique.gestionDonnees.viderDonnees();
        Saltistique.changeScene(Scenes.ACCUEIL);

        new Notification("Données déchargées", "Les données ont été déchargées avec succès.");
    }

    /**
     * Gère le retour au menu principal.
     */
    @FXML
    public void handlerRetourMenu() {
        if (conteneurMenu.isVisible()) {
            menuClick();
        }
    }

    /**
     * Gère le clic sur le bouton de partage pour exporter les données.
     * Affiche un popup pour exporter les données sur le réseau.
     */
    @FXML
    void handlerPartager() {
        Saltistique.showPopUp(Scenes.EXPORTER_RESEAU);
    }

    /**
     * Ajoute les effets de survol sur les boutons de la barre de navigation.
     */
    public void setHoverEffect() {
        btnFermer.setOnMouseEntered(_ -> iconeFermetureFenetre.getStyleClass().add("bg-gray-light"));
        btnFermer.setOnMouseExited(_ -> iconeFermetureFenetre.getStyleClass().remove("bg-gray-light"));
    }

    /**
     * Configure l'action du bouton de notification pour masquer le cadre de notification
     * et le rendre transparent aux interactions de la souris.
     */
    public void clickBoutonNotification() {
        boutonNotification.setOnAction(_ -> {
            cadreNotification.setVisible(false);
            cadreNotification.setMouseTransparent(true);
        });
    }

    @FXML
    void clickGenererPDF() {
        //TODO
    }
}
