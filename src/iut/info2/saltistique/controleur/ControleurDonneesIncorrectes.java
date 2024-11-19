/*
 * ControleurConsulterDonnees.java 02/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 *
 * @author Jules VIALAS, Tom GUTIERREZ
 */

package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.Scenes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Line;

/**
 * Le contrôleur de la vue permettant de consulter les données.
 * Cette classe gère l'affichage et l'interaction avec les tables de données
 * (réservations, activités, employés, salles) ainsi que le contrôle des boutons
 * et autres éléments interactifs de l'interface.
 */
public class ControleurDonneesIncorrectes extends Controleur {

    /**
     * Bouton pour afficher les employés.
     */
    @FXML
    public Button employes;

    /**
     * Bouton pour afficher les activités.
     */
    @FXML
    public Button activites;

    /**
     * Bouton pour afficher les réservations.
     */
    @FXML
    public Button reservations;

    /**
     * Bouton pour afficher les salles.
     */
    @FXML
    public Button salles;

    /**
     * Ligne de sélection pour le bouton "Salles".
     */
    @FXML
    public Line SelectionSalles;

    /**
     * Ligne de sélection pour le bouton "Activités".
     */
    @FXML
    public Line SelectionActivites;

    /**
     * Ligne de sélection pour le bouton "Employés".
     */
    @FXML
    public Line SelectionEmployes;

    /**
     * Ligne de sélection pour le bouton "Réservations".
     */
    @FXML
    public Line SelectionReservations;

    /**
     * Tableau des salles.
     */
    @FXML
    private TableView<String[]> tableauSalles;

    /**
     * Tableau des activités.
     */
    @FXML
    private TableView<String[]> tableauActivites;

    /**
     * Tableau des employés.
     */
    @FXML
    private TableView<String[]> tableauEmployes;

    /**
     * Tableau des réservations.
     */
    @FXML
    private TableView<String[]> tableauReservations;

    /**
     * Colonne de la ligne de la salle.
     */
    @FXML
    private TableColumn<String[], String> LigneSalles;

    /**
     * Colonne de l'erreur de la ligne Salle.
     */
    @FXML
    private TableColumn<String[], String> ErreurSalles;

    /**
     * Colonne de la ligne de l'activité.
     */
    @FXML
    private TableColumn<String[], String> LigneActivites;

    /**
     * Colonne de l'erreur de la ligne Activité.
     */
    @FXML
    private TableColumn<String[], String> ErreurActivites;

    /**
     * Colonne de la ligne de l'employé.
     */
    @FXML
    private TableColumn<String[], String> LigneEmployes;

    /**
     * Colonne de l'erreur de la ligne Employé.
     */
    @FXML
    private TableColumn<String[], String> ErreurEmployes;

    /**
     * Colonne de la ligne de la réservation.
     */
    @FXML
    private TableColumn<String[], String> LigneReservations;

    /**
     * Colonne de l'erreur de la ligne Réservation.
     */
    @FXML
    private TableColumn<String[], String> ErreurReservations;


    /**
     * Listes observables contenant les objets de chaques types.
     * Ces listes sont utilisées pour afficher et gérer les types de données disponibles
     * dans l'application, permettant la liaison de données pour des composants
     * de l'interface utilisateur, tels que des tableaux ou des listes.
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
        clickBoutonNotification();
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
        listeSalles.addAll(Saltistique.gestionDonnees.getLignesIncorrectesSalles());
        listeActivites.addAll(Saltistique.gestionDonnees.getLignesIncorrectesActivites());
        listeEmployes.addAll(Saltistique.gestionDonnees.getLignesIncorrectesUtilisateurs());
        listeReservations.addAll(Saltistique.gestionDonnees.getLignesIncorrectesReservations());
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
     * Affiche le tableau et la sélection spécifiés, tout en masquant les autres.
     *
     * @param tableau   La référence au Node représentant le tableau à afficher.
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
     * Gère la génération d'un rapport PDF
     */
    @FXML
    public void clickGenererPDF() {
        //TODO
    }
}