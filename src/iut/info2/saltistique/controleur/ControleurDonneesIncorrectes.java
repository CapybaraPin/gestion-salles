/*
 * ControleurConsulterDonnees.java 02/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 *
 * @author Jules VIALAS & Tom GUTIERREZ
 */

package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import static iut.info2.saltistique.Saltistique.gestionDonnees;

/**
 * Le contrôleur de la vue permettant de consulter les données.
 * Cette classe gère l'affichage et l'interaction avec les tables de données
 * (réservations, activités, employés, salles) ainsi que le contrôle des boutons
 * et autres éléments interactifs de l'interface.
 *
 */
public class ControleurDonneesIncorrectes {

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
    public VBox notificationFrame;

    @FXML
    public Text notificationTitre;

    @FXML
    public Text notificationDescription;

    @FXML
    public Button notificationBouton;

    /** Tableau des salles. */
    @FXML
    private TableView<String[]> tableauSalles;

    /** Tableau des activités. */
    @FXML
    private TableView<String[]> tableauActivites;

    /** Tableau des employés. */
    @FXML
    private TableView<String[]> tableauEmployes;

    /** Tableau des réservations. */
    @FXML
    private TableView<String[]> tableauReservations;

    /** Colonne de la ligne de la salle. */
    @FXML
    private TableColumn<String[], String> LigneSalles;

    /** Colonne de l'erreur de la ligne Salle. */
    @FXML
    private TableColumn<String[], String> ErreurSalles;

    /** Colonne de la ligne de l'activité. */
    @FXML
    private TableColumn<String[], String> LigneActivites;

    /** Colonne de l'erreur de la ligne Activité. */
    @FXML
    private TableColumn<String[], String> ErreurActivites;

    /** Colonne de la ligne de l'employé. */
    @FXML
    private TableColumn<String[], String> LigneEmployes;

    /** Colonne de l'erreur de la ligne Employé. */
    @FXML
    private TableColumn<String[], String> ErreurEmployes;

    /** Colonne de la ligne de la réservation. */
    @FXML
    private TableColumn<String[], String> LigneReservations;

    /** Colonne de l'erreur de la ligne Réservation. */
    @FXML
    private TableColumn<String[], String> ErreurReservations;


    /**
     * Listes observables contenant les objets de chaques types.
     * Ces listes sont utilisées pour afficher et gérer les types de données disponibles
     * dans l'application, permettant la liaison de données pour des composants
     * de l'interface utilisateur, tels que des tableaux ou des listes.
     *
     */
    private ObservableList<String[]> listeSalles;
    private ObservableList<String[]> listeActivites;
    private ObservableList<String[]> listeEmployes;
    private ObservableList<String[]> listeReservations;

    /**
     * Initialise le contrôleur et configure les tableaux.
     */
    public void initialize() {
        listeSalles = FXCollections.observableArrayList();
        listeActivites = FXCollections.observableArrayList();
        listeEmployes = FXCollections.observableArrayList();
        listeReservations = FXCollections.observableArrayList();
        initialiserTableaux();
        initialiserTableauSalles();
        initialiserTableauActivites();
        initialiserTableauEmployes();
        initialiserTableauReservations();

        notificationBouton.setOnAction(event -> { // TODO : enlever ce code
            notificationFrame.setVisible(false);
            notificationFrame.setMouseTransparent(true);
        });
    }

    /**
     * Initialise les listes de salles, d'activités, d'employés et de réservations
     * incorrectes *
     * Ces listes sont initialisées en tant que listes observables et sont utilisées
     * en les remplissant avec les données fournies par le gestionnaire de données.
     * Cette méthode parcourt les entrées des collections de données et les ajoute
     * aux listes correspondantes.
     */
    private void initialiserTableaux() {
        listeSalles.addAll(gestionDonnees.getLignesIncorrectesSalles());
        listeActivites.addAll(gestionDonnees.getLignesIncorrectesActivites());
        listeEmployes.addAll(gestionDonnees.getLignesIncorrectesUtilisateurs());
        listeReservations.addAll(gestionDonnees.getLignesIncorrectesReservations());
    }

    /**
     * Méthode pour rafraîchir les tableaux après l'importation de nouvelles données.
     */
    public void rafraichirTableaux() {
        listeSalles.clear();
        listeActivites.clear();
        listeEmployes.clear();
        listeReservations.clear();
        initialiserTableaux();
        tableauSalles.setItems(listeSalles);
        tableauActivites.setItems(listeActivites);
        tableauEmployes.setItems(listeEmployes);
        tableauReservations.setItems(listeReservations);
    }

    /**
     * Initialise le tableau des salles.
     */
    private void initialiserTableauSalles() {
        LigneSalles.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        ErreurSalles.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        tableauSalles.setItems(listeSalles);
    }

    /**
     * Initialise le tableau des activités.
     */
    private void initialiserTableauActivites() {
        LigneActivites.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        ErreurActivites.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        tableauActivites.setItems(listeActivites);
    }

    /**
     * Initialise le tableau des employés.
     */
    private void initialiserTableauEmployes() {
        LigneEmployes.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        ErreurEmployes.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        tableauEmployes.setItems(listeEmployes);
    }

    /**
     * Initialise le tableau des réservations.
     */
    private void initialiserTableauReservations() {
        LigneReservations.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        ErreurReservations.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        tableauReservations.setItems(listeReservations);
    }

    /**
     * Gère le clic sur le bouton de consultation des données.
     */
    @FXML
    private void chargerSceneConsulterDonnees() {
        Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
        ControleurConsulterDonnees controleur = Saltistique.getController(Scenes.CONSULTER_DONNEES);
        controleur.rafraichirTableaux();
        new Notification("Données importées", "Les données ont été importées avec succès.");
    }

    /**
     * Gère le clic sur le bouton de réimportation des données.
     */
    @FXML
    private void chargerSceneAccueil() {
        Saltistique.changeScene(Scenes.ACCUEIL);
    }


    /**
     * Gère le clic sur le bouton de chargement des données.
     * Vide les données du modèle et change de scène pour l'accueil.
     * Affiche une notification pour informer l'utilisateur du succès de l'opération.
     */
    @FXML
    void clickDechargerDonnees() {
        if (menuContainer.isVisible()) {
            burgerClicked();
        }

        gestionDonnees.viderDonnees();
        Saltistique.changeScene(Scenes.ACCUEIL);

        new Notification("Données déchargées", "Les données ont été déchargées avec succès.");
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
     * Affiche le tableau et la sélection spécifiés, tout en masquant les autres.
     *
     * @param tableau La référence au Node représentant le tableau à afficher.
     * @param selection La référence au Node représentant la sélection à afficher.
     */
    @FXML
    private void afficherTableau(Node tableau, Node selection) {
        masquerTousLesTableauxEtSelections();
        tableau.setVisible(true);
        selection.setVisible(true);
    }

    /**
     * Masque tous les tableaux et les sélections dans l'interface utilisateur.
     * Cette méthode est utilisée pour garantir qu'aucun tableau ou sélection
     * n'est affiché avant de montrer les éléments spécifiques requis.
     */
    @FXML
    private void masquerTousLesTableauxEtSelections() {
        tableauSalles.setVisible(false);
        tableauReservations.setVisible(false);
        tableauActivites.setVisible(false);
        tableauEmployes.setVisible(false);
        SelectionSalles.setVisible(false);
        SelectionActivites.setVisible(false);
        SelectionEmployes.setVisible(false);
        SelectionReservations.setVisible(false);
    }

    /**
     * Affiche le tableau des salles et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des salles et la sélection des salles,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauSalles() {
        afficherTableau(tableauSalles, SelectionSalles);
    }

    /**
     * Affiche le tableau des réservations et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des réservations et la sélection des réservations,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauReservations() {
        afficherTableau(tableauReservations, SelectionReservations);
    }

    /**
     * Affiche le tableau des activités et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des activités et la sélection des activités,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauActivites() {
        afficherTableau(tableauActivites, SelectionActivites);
    }

    /**
     * Affiche le tableau des employés et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des employés et la sélection des employés,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauEmployes() {
        afficherTableau(tableauEmployes, SelectionEmployes);
    }

    /**
     * Gère le retour au menu principal.
     */
    @FXML
    public void handlerRetourMenu() {
        if (menuContainer.isVisible()) {
            burgerClicked();
        }
        chargerSceneConsulterDonnees();
    }

    /**
     * Gère la génération d'un rapport PDF
     */
    @FXML
    public void clickGenererPDF() {
        //TODO
    }
}