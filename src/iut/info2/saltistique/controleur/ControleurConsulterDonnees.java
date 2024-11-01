package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Salle;
import iut.info2.saltistique.modele.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.TableColumn;
import static iut.info2.saltistique.Saltistique.gestionDonnees;

/**
 * Le contrôleur de la vue permettant de consulter les données.
 * Cette classe gère l'affichage et l'interaction avec les tables de données
 * (réservations, activités, employés, salles) ainsi que le contrôle des boutons
 * et autres éléments interactifs de l'interface.
 */
public class ControleurConsulterDonnees {

    /** TableView pour les réservations. */
    @FXML
    public TableView tableauReservations;

    /** TableView pour les activités. */
    @FXML
    public TableView tableauActivites;

    /** TableView pour les employés. */
    @FXML
    public TableView tableauEmployes;

    /** TableView pour les salles. */
    @FXML
    public TableView tableauSalles;

    /** Bouton pour afficher les employés. */
    @FXML
    public Button employes;

    /** Bouton pour afficher les activités. */
    @FXML
    public Button activites;

    /** Bouton pour afficher les réservations. */
    @FXML
    public Button reservations;

    /** Bouton pour afficher les salles. */
    @FXML
    public Button salles;

    /** Ligne de sélection pour le bouton "Salles". */
    @FXML
    public Line SelectionSalles;

    /** Ligne de sélection pour le bouton "Activités". */
    @FXML
    public Line SelectionActivites;

    /** Ligne de sélection pour le bouton "Employés". */
    @FXML
    public Line SelectionEmployes;

    /** Ligne de sélection pour le bouton "Réservations". */
    @FXML
    public Line SelectionReservations;

    /** Position horizontale de la souris pour le déplacement de la fenêtre. */
    double xOffset = 0;

    /** Position verticale de la souris pour le déplacement de la fenêtre. */
    double yOffset = 0;

    /** Conteneur pour le menu de navigation. */
    @FXML
    private VBox menuContainer;

    /** Couche utilisée pour fermer le menu de navigation. */
    @FXML
    private Pane layerMenu;

    @FXML
    private TableColumn<Salle, Integer> IdentifiantSalle;

    @FXML
    private TableColumn<Salle, String> NomSalle;

    @FXML
    private TableColumn<Salle, Integer> Capacite;

    @FXML
    private TableColumn<Salle, Boolean> VideoProjecteur;

    @FXML
    private TableColumn<Salle, Boolean> EcranXXL;

    @FXML
    private TableColumn<Salle, Integer> Ordinateurs;

    @FXML
    private TableColumn<Salle, String> Type;

    @FXML
    private TableColumn<Salle, String> Logiciels;

    @FXML
    private TableColumn<Salle, Boolean> Imprimante;

    public static void initialiserTableaux() {
        System.out.print(gestionDonnees.getSalles().size());
        System.out.print(gestionDonnees.getSalles().size());
        System.out.print(gestionDonnees.getSalles().size());
        // TODO Importer les salles dans les tableaux;
    }

    /**
     * Gère le clic sur le bouton de partage pour exporter les données.
     * Affiche une popup pour exporter les données sur le réseau.
     */
    @FXML
    void handlerPartager() {
        Saltistique.showPopUp(Scenes.EXPORTER_RESEAU);
    }

    /**
     * Gère le clic sur le bouton d'aide.
     * Affiche l'aide utilisateur sous forme d'un fichier PDF.
     */
    @FXML
    void handlerAide() {
        clickAide();
        if (menuContainer.isVisible()) {
            burgerClicked();
        }
    }

    /**
     * Ouvre le fichier PDF d'aide utilisateur.
     */
    private void clickAide() {
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
     * Ferme la fenêtre active.
     * @param event L'événement de souris déclenché lors du clic pour fermer la fenêtre.
     */
    @FXML
    void fermerFenetre(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Réduit la fenêtre active.
     * @param event L'événement de souris déclenché lors du clic pour réduire la fenêtre.
     */
    @FXML
    void reduireFenetre(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Affiche ou masque le menu de navigation.
     */
    @FXML
    void burgerClicked() {
        if (menuContainer.isVisible()) {
            menuContainer.setVisible(false);
            menuContainer.setMouseTransparent(true);
            layerMenu.setMouseTransparent(true);
        } else {
            menuContainer.setVisible(true);
            menuContainer.setMouseTransparent(false);
            layerMenu.setMouseTransparent(false);
        }
    }

    /**
     * Capture les coordonnées de la souris lors d'un clic.
     * Utilisé pour le déplacement de la fenêtre.
     * @param event L'événement de souris.
     */
    @FXML
    void clicked(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    /**
     * Déplace la fenêtre en fonction des coordonnées de la souris.
     * @param event L'événement de souris déclenché lors du déplacement.
     */
    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    /**
     * Affiche le tableau des salles et masque les autres tableaux.
     * Active également la ligne de sélection correspondante.
     */
    @FXML
    void afficherTableauSalles() {
        tableauSalles.setVisible(true);
        tableauReservations.setVisible(false);
        tableauActivites.setVisible(false);
        tableauEmployes.setVisible(false);

        SelectionSalles.setVisible(true);
        SelectionActivites.setVisible(false);
        SelectionEmployes.setVisible(false);
        SelectionReservations.setVisible(false);
    }

    /**
     * Affiche le tableau des réservations et masque les autres tableaux.
     * Active également la ligne de sélection correspondante.
     */
    @FXML
    void afficherTableauReservations() {
        tableauSalles.setVisible(false);
        tableauReservations.setVisible(true);
        tableauActivites.setVisible(false);
        tableauEmployes.setVisible(false);

        SelectionSalles.setVisible(false);
        SelectionActivites.setVisible(false);
        SelectionEmployes.setVisible(false);
        SelectionReservations.setVisible(true);
    }

    /**
     * Affiche le tableau des activités et masque les autres tableaux.
     * Active également la ligne de sélection correspondante.
     */
    @FXML
    void afficherTableauActivites() {
        tableauSalles.setVisible(false);
        tableauReservations.setVisible(false);
        tableauActivites.setVisible(true);
        tableauEmployes.setVisible(false);

        SelectionSalles.setVisible(false);
        SelectionActivites.setVisible(true);
        SelectionEmployes.setVisible(false);
        SelectionReservations.setVisible(false);
    }

    /**
     * Affiche le tableau des employés et masque les autres tableaux.
     * Active également la ligne de sélection correspondante.
     */
    @FXML
    void afficherTableauEmployes() {
        tableauSalles.setVisible(false);
        tableauReservations.setVisible(false);
        tableauActivites.setVisible(false);
        tableauEmployes.setVisible(true);

        SelectionSalles.setVisible(false);
        SelectionActivites.setVisible(false);
        SelectionEmployes.setVisible(true);
        SelectionReservations.setVisible(false);
    }

    /**
     * Gère le retour au menu principal.
     */
    public void handlerRetourMenu() {
        Saltistique.changeScene(Scenes.ACCUEIL);
    }

    /**
     * Gère le click pour générer un rapport pdf
     * @param actionEvent
     */
    public void clickGenererPDF(ActionEvent actionEvent) {
        if (SelectionSalles.isVisible()) {
            //TODO récupérer le tableau des salles et afficher dans un pdf
        } else if (SelectionActivites.isVisible()) {
            //TODO récupérer le tableau des activites et afficher dans un pdf
        } else if (SelectionEmployes.isVisible()) {
            //TODO récupérer le tableau des employes et afficher dans un pdf
        } else if (SelectionReservations.isVisible()) {
            //TODO récupérer le tableau des reservations et afficher dans un pdf
        }
    }
}