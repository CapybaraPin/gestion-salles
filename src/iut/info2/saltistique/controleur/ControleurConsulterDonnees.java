package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import javafx.scene.control.TableColumn;

/**
 * Le contrôleur de la vue permettant de consulter les données.
 * Cette classe gère l'affichage et l'interaction avec les tables de données
 * (réservations, activités, employés, salles) ainsi que le contrôle des boutons
 * et autres éléments interactifs de l'interface.
 */
public class ControleurConsulterDonnees {

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

    /** Colonne pour l'identifiant de l'employé. */
    @FXML
    public TableColumn<Utilisateur, String> IdentifiantEmploye;

    /** Colonne pour le nom de l'employé. */
    @FXML
    public TableColumn<Utilisateur, String> NomEmploye;

    /** Colonne pour le prénom de l'employé. */
    @FXML
    public TableColumn<Utilisateur, String> Prenom;

    /** Colonne pour le numéro de téléphone de l'employé. */
    @FXML
    public TableColumn<Utilisateur, String> Numero_de_telephone;

    /** Colonne pour l'identifiant de l'activité. */
    @FXML
    public TableColumn<Activite, String> IdentifiantActivite;

    /** Colonne pour le nom de l'activité. */
    @FXML
    public TableColumn<Activite, String> Nom;

    /** Colonne pour l'identifiant de la réservation. */
    @FXML
    public TableColumn<Reservation, String> IdentifiantReservation;

    /** Colonne pour la date de début de la réservation. */
    @FXML
    public TableColumn<Reservation, LocalDateTime> Date_de_debut;

    /** Colonne pour la date de fin de la réservation. */
    @FXML
    public TableColumn<Reservation, LocalDateTime> Date_de_fin;

    /** Colonne pour la description de la réservation. */
    @FXML
    public TableColumn<Reservation, String> Description;

    /** Colonne pour la salle associée à la réservation. */
    @FXML
    public TableColumn<Reservation, Salle> Salle;

    /** Colonne pour l'activité associée à la réservation. */
    @FXML
    public TableColumn<Reservation, Activite> Activite;

    /** Colonne pour l'utilisateur associé à la réservation. */
    @FXML
    public TableColumn<Reservation, Utilisateur> Utilisateur;

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

    /** TableView pour les réservations. */
    @FXML
    public TableView<Reservation> tableauReservations;

    /** TableView pour les employés. */
    @FXML
    public TableView<Utilisateur> tableauEmployes;

    /** TableView pour les activités. */
    @FXML
    public TableView<Activite> tableauActivites;

    /** TableView pour les salles. */
    @FXML
    public TableView<Salle> tableauSalles;

    /** Colonne pour l'identifiant de la salle. */
    @FXML
    public TableColumn<Salle, String> IdentifiantSalle;

    /** Colonne pour le nom de la salle. */
    @FXML
    public TableColumn<Salle, String> NomSalle;

    /** Colonne pour la capacité de la salle. */
    @FXML
    public TableColumn<Salle, Integer> Capacite;

    /** Colonne pour la disponibilité d'un vidéo projecteur. */
    @FXML
    public TableColumn<Salle, Boolean> VideoProjecteur;

    /** Colonne pour la disponibilité d'un écran XXL. */
    @FXML
    public TableColumn<Salle, Boolean> EcranXXL;

    /** Colonne pour les ordinateurs disponibles dans la salle. */
    @FXML
    public TableColumn<Salle, String> Ordinateurs;

    /** Colonne pour le type de la salle. */
    @FXML
    public TableColumn<Salle, String> Type;

    /** Colonne pour les logiciels disponibles dans la salle. */
    @FXML
    public TableColumn<Salle, String> Logiciels;

    /** Colonne pour la disponibilité d'une imprimante. */
    @FXML
    public TableColumn<Salle, Boolean> Imprimante;

    /**
     * Initialise le contrôleur et configure les tableaux.
     */
    public void initialize() {
        initialiserTableauSalles();
        initialiserTableauActivites();
        initialiserTableauEmployes();
        initialiserTableauReservations();
    }

    /**
     * Initialise le tableau des activités.
     */
    public void initialiserTableauActivites() {
        IdentifiantActivite.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableauActivites.setItems(listeActivites);
    }

    /**
     * Initialise le tableau des employés.
     */
    public void initialiserTableauEmployes() {
        IdentifiantEmploye.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        NomEmploye.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        Numero_de_telephone.setCellValueFactory(new PropertyValueFactory<>("numeroTelephone"));
        tableauEmployes.setItems(listeEmployes);
    }

    /**
     * Initialise le tableau des réservations.
     */
    public void initialiserTableauReservations() {
        IdentifiantReservation.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        Date_de_debut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        Date_de_fin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Salle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        Activite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        Utilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        tableauReservations.setItems(listeReservations);
    }

    /**
     * Initialise le tableau des salles.
     */
    public void initialiserTableauSalles() {
        IdentifiantSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        NomSalle.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Capacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        VideoProjecteur.setCellValueFactory(new PropertyValueFactory<>("videoProjecteur"));
        EcranXXL.setCellValueFactory(new PropertyValueFactory<>("ecranXXL"));
        Ordinateurs.setCellValueFactory(new PropertyValueFactory<>("ordinateurs"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Logiciels.setCellValueFactory(new PropertyValueFactory<>("logiciels"));
        Imprimante.setCellValueFactory(new PropertyValueFactory<>("imprimante"));
        tableauSalles.setItems(listeSalles);
    }

    /** Liste des salles. */
    private final ObservableList<Salle> listeSalles = FXCollections.observableArrayList(
            new Salle("1", "bonjour", 1, true, true, true, new GroupeOrdinateurs(1, "coucou", new String[]{})));

    /** Liste des activités. */
    private final ObservableList<Activite> listeActivites = FXCollections.observableArrayList(
            new Activite("A1", "Yoga"),
            new Activite("A2", "Piano"),
            new Activite("A3", "Danse"),
            new Activite("A4", "Cours de cuisine")
    );

    /** Liste des employés. */
    private final ObservableList<Utilisateur> listeEmployes = FXCollections.observableArrayList(
            new Utilisateur("E1", "Dupont", "Jean", "0123456789"),
            new Utilisateur("E2", "Martin", "Sophie", "0987654321"),
            new Utilisateur("E3", "Durand", "Alice", "0147258369"),
            new Utilisateur("E4", "Lemoine", "Paul", "0172589634")
    );

    /** Liste des réservations. */
    private final ObservableList<Reservation> listeReservations = FXCollections.observableArrayList(
            new Reservation("R1",
                    LocalDateTime.of(2024, 11, 10, 14, 0),
                    LocalDateTime.of(2024, 11, 10, 16, 0),
                    "Réunion projet",
                    new Salle("1", "Salle A", 30, true, true, true, new GroupeOrdinateurs(1, "Groupe 1", new String[]{})),
                    new Activite("A1", "Yoga"),
                    new Utilisateur("E4", "Lemoine", "Paul", "0172589634"))
    );
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