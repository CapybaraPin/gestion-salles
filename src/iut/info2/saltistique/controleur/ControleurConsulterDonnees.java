/*
 * ControleurConsulterDonnees.java 02/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Le contrôleur de la vue permettant de consulter les données.
 * Gère l'affichage, le filtrage, et l'interaction avec les tables de données
 * (réservations, activités, employés, salles) ainsi que le contrôle des boutons
 * et autres éléments interactifs de l'interface.
 * <p>
 * Ce contrôleur est responsable de :
 * <ul>
 *     <li>L'initialisation des tableaux pour afficher les données.</li>
 *     <li>L'application et la gestion des filtres pour les données visibles.</li>
 *     <li>La gestion de la navigation entre les différentes catégories de données.</li>
 * </ul>
 *
 * @author Jules VIALAS
 */
public class ControleurConsulterDonnees extends Controleur {

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
    public TableColumn<Utilisateur, String> numeroDeTelephone;

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
    public TableColumn<Reservation, LocalDateTime> dateDeDebut;

    /** Colonne pour la date de fin de la réservation. */
    @FXML
    public TableColumn<Reservation, LocalDateTime> dateDeFin;

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

    /** Colonne pour la disponibilité d'une vidéo projecteur. */
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

    /** Colonne pour les logiciels disponibles dans la salle.  */
    @FXML
    public TableColumn<Salle, String> Logiciels;

    /** Colonne pour la disponibilité d'une imprimante. */
    @FXML
    public TableColumn<Salle, Boolean> Imprimante;


    /** Filtre contenant les différents filtres appliqués */
    @FXML
    private Filtre filtre;

    /** Afficher les statistiques d'une salle */
    @FXML
    private TableColumn<Salle, Void> Actions;

    /**
     * Listes observables contenant les objets de chaques types.
     * Ces listes sont utilisées pour afficher et gérer les types de données disponibles
     * dans l'application, permettant la liaison de données pour des composants
     * de l'interface utilisateur, tels que des tableaux ou des listes.
     */
    protected ObservableList<Salle> listeSalles;
    protected ObservableList<Activite> listeActivites;
    protected ObservableList<Utilisateur> listeEmployes;
    protected ObservableList<Reservation> listeReservations;

    /**
     * Initialise le contrôleur après le chargement de la vue FXML.
     * Configure les filtres, les listes observables, et initialise les tableaux.
     */
    public void initialize() {
        listeSalles = FXCollections.observableArrayList();
        listeActivites = FXCollections.observableArrayList();
        listeEmployes = FXCollections.observableArrayList();
        listeReservations = FXCollections.observableArrayList();

        // Initialisation des filtres
        filtre = new Filtre();
        setFiltre(filtre);
        actualiserFiltres();
        initialiserFiltres();

        // Initialisation des tableaux
        initialiserTableaux();
        initialiserTableauSalles();
        initialiserTableauActivites();
        initialiserTableauEmployes();
        initialiserTableauReservations();
        clickBoutonNotification();
    }

    /**
     * Permet l'actualisation des données dans le contrôleur
     * de gestion des filtres.
     */
    void actualiserFiltres() {
        setListeSalles(listeSalles);
        setListeActivites(listeActivites);
        setListeEmployes(listeEmployes);
        setListeReservations(listeReservations);
    }

    /**
     * Rafraîchit les données affichées dans les tableaux.
     * Recharge les données depuis la source et met à jour les tableaux liés.
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
        afficherTempsReservationsTotal(listeReservations);
    }

    /**
     * Initialise les listes de salles, d'activités, d'employés et de réservations
     * en les remplissant avec les données fournies par le gestionnaire de données.
     * Cette méthode parcourt les entrées des collections de données et les ajoute
     * aux listes correspondantes.
     */
    void initialiserTableaux() {
        for (Map.Entry<Integer, Salle> entry : Saltistique.gestionDonnees.getSalles().entrySet()) {
            listeSalles.add(entry.getValue());
        }
        for (Map.Entry<Integer, Activite> entry : Saltistique.gestionDonnees.getActivites().entrySet()) {
            listeActivites.add(entry.getValue());
        }
        for (Map.Entry<Integer, Utilisateur> entry : Saltistique.gestionDonnees.getUtilisateurs().entrySet()) {
            listeEmployes.add(entry.getValue());
        }
        for (Map.Entry<Integer, Reservation> entry : Saltistique.gestionDonnees.getReservations().entrySet()) {
            listeReservations.add(entry.getValue());
        }
    }

    /**
     * Initialise le tableau des salles.
     */
    private void initialiserTableauSalles() {
        IdentifiantSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        NomSalle.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Capacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        VideoProjecteur.setCellValueFactory(new PropertyValueFactory<>("videoProjecteur"));
        EcranXXL.setCellValueFactory(new PropertyValueFactory<>("ecranXXL"));
        Ordinateurs.setCellValueFactory(new PropertyValueFactory<>("ordinateurs"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Logiciels.setCellValueFactory(new PropertyValueFactory<>("logiciels"));
        Imprimante.setCellValueFactory(new PropertyValueFactory<>("imprimante"));

        Actions.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Charger l'icône
                    ImageView svgIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ressources/icons/chart.png"))));
                    svgIcon.setFitWidth(16); // Ajuster la largeur de l'icône
                    svgIcon.setFitHeight(16); // Ajuster la hauteur de l'icône

                    // Créer le bouton avec un texte
                    Button btnAction = new Button("Statistique");
                    btnAction.getStyleClass().add("btn-secondary");

                    // Ajouter l'icône au bouton
                    btnAction.setGraphic(svgIcon);

                    // Ajouter une action au bouton
                    btnAction.setOnAction(_ -> {
                        // Récupérer la salle sélectionnée
                        Salle salleSelectionnee = getTableView().getItems().get(getIndex());
                        System.out.println(salleSelectionnee);
                        // Appeler la méthode pour afficher la nouvelle vue
                        afficherConsultationSalle(salleSelectionnee);
                    });

                    setGraphic(btnAction); // Ajouter le bouton à la cellule
                    setText(null);
                }
            }
        });

        tableauSalles.setItems(listeSalles);
    }

    /**
     * Charge la vue de consultation de salle et passe les données de la salle sélectionnée.
     * @param salle La salle sélectionnée à afficher.
     */
    private void afficherConsultationSalle(Salle salle) {
        ControleurConsultationSalle controleur = Saltistique.getController(Scenes.CONSULTER_SALLE);
        controleur.setSalle(salle);
        controleur.setDonneesFiltres(listeReservations, listeActivites, listeEmployes, listeSalles);
        controleur.actualiserStats();
        Saltistique.showPopUp(Scenes.CONSULTER_SALLE);
    }

    /**
     * Initialise le tableau des activités.
     */
    private void initialiserTableauActivites() {
        IdentifiantActivite.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableauActivites.setItems(listeActivites);
    }

    /**
     * Initialise le tableau des employés.
     */
    private void initialiserTableauEmployes() {
        IdentifiantEmploye.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        NomEmploye.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        numeroDeTelephone.setCellValueFactory(new PropertyValueFactory<>("numeroTelephone"));
        tableauEmployes.setItems(listeEmployes);
    }

    /**
     * Initialise le tableau des réservations.
     */
    private void initialiserTableauReservations() {
        IdentifiantReservation.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        dateDeDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateDeFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Salle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        Activite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        Utilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        tableauReservations.setItems(listeReservations);
    }

    /**
     * Affiche uniquement le tableau et la sélection spécifiés.
     * Masque les autres tableaux et sélections.
     *
     * @param tableau   Le tableau a affiché.
     * @param selection La sélection a affiché.
     */
    @FXML
    private void afficherTableau(Node tableau, Node selection) {
        masquerTousLesTableauxEtSelections();
        tableau.setVisible(true);
        selection.setVisible(true);
        boolean afficherFiltres = tableau == tableauReservations;
        valeurFiltre.setVisible(afficherFiltres);
        Filtres.setVisible(afficherFiltres);
        boutonFiltrer.setVisible(afficherFiltres);
        if (Filtres.getSelectionModel().getSelectedItem().equals("Période")) {
            vboxFiltreDate.setVisible(afficherFiltres);
        } else {
            hboxFiltreTexte.setVisible(afficherFiltres);
        }
        hboxFiltresAppliques.setVisible(afficherFiltres);
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
     * Affiche le tableau des reservations et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des reservations et la sélection des reservations,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauReservations() {
        afficherTableau(tableauReservations, SelectionReservations);
    }

    /**
     * Affiche le tableau des activites et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des activites et la sélection des activites,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauActivites() {
        afficherTableau(tableauActivites, SelectionActivites);
    }

    /**
     * Affiche le tableau des employes et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des employes et la sélection des employes,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauEmployes() {
        afficherTableau(tableauEmployes, SelectionEmployes);
    }

    @FXML
    void clickStatistiques() {
        Saltistique.changeScene(Scenes.CONSULTER_STATISTIQUES);
    }
}